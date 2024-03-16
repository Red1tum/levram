package com.reditum.marvel.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.data.MarvelApi
import com.reditum.marvel.data.REQUEST_LIMIT
import com.reditum.marvel.db.CharacterDatabase
import com.reditum.marvel.db.entities.CharacterEntity
import com.reditum.marvel.db.entities.OffsetEntity
import com.reditum.marvel.ui.theme.getNetworkImageColor
import com.reditum.marvel.ui.theme.getPrimaryColors
import com.reditum.marvel.ui.theme.isDark
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val db: CharacterDatabase,
    @ApplicationContext val context: Context,
) : ViewModel() {
    var errored = MutableStateFlow(false)

    private lateinit var offset: OffsetEntity

    val characters = MutableStateFlow<List<CharacterEntity>>(emptyList())

    fun updateColors(isDark: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            characters.value = characters.value.map { char ->
                char.copy(
                    primaryColors = getPrimaryColors(char.thumbnailColor, isDark)
                )
            }
        }
    }


    fun load() {
        errored.value = false
        viewModelScope.launch(Dispatchers.IO) {
            MarvelApi.getHeroes(offset.value)
                .onSuccess { res ->
                    offset.value += REQUEST_LIMIT
                    val isDark = context.resources.configuration.isDark()
                    val chars = res.map { char ->
                        val color = getNetworkImageColor(context, char.thumbnail.getUrl()).toArgb()
                        val entity = CharacterEntity(
                            char.id,
                            char.name,
                            char.description,
                            char.thumbnail.getUrl(),
                            color,
                            getPrimaryColors(color, isDark)
                        )
                        entity
                    }
                    characters.value += chars
                    db.transaction {
                        chars.onEach(db::upsert)
                        db.upsert(offset)
                    }
                }.onFailure {
                    errored.value = true
                    Log.e("HomeViewModel", it.toString())
                }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // not ideal, but I haven't fully grasped
            // db preseeding so keeping it as is
            offset = if (db.isOffsetNotEmpty() == 0) {
                OffsetEntity(value = 0)
            } else {
                db.offset()
            }

            val chars = db.characterList()
            if (chars.isNotEmpty()) {
                val isDark = context.resources.configuration.isDark()
                characters.value = chars.onEach {
                    it.primaryColors = getPrimaryColors(it.thumbnailColor, isDark)
                }
            } else {
                load()
            }
        }

    }
}