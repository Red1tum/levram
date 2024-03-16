package com.reditum.marvel.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.data.MarvelApi
import com.reditum.marvel.db.CharacterDatabase
import com.reditum.marvel.db.entities.CharacterEntity
import com.reditum.marvel.db.entities.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val db: CharacterDatabase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val error = MutableStateFlow(false)
    private val heroId = savedStateHandle.get<String>("id")!!.toInt()

    val hero = MutableStateFlow<CharacterEntity?>(null)

    suspend fun getHero(): CharacterEntity {
        error.value = false
        val hero = MarvelApi.getHero(heroId).getOrNull()
        if (hero == null) error.value = true
        val entity = hero!!.toEntity(context)
        db.upsert(entity)
        return entity

    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val heroDb = db.character(heroId)
            if (heroDb == null) {
                val heroNet = getHero()
                hero.value = heroNet
                db.upsert(heroNet)

            } else {
                hero.value = heroDb
            }

        }
    }
}