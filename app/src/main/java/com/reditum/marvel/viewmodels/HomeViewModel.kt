package com.reditum.marvel.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.data.Character
import com.reditum.marvel.data.MarvelApi
import com.reditum.marvel.data.REQUEST_LIMIT
import com.reditum.marvel.ui.theme.getNetworkImageColor
import com.reditum.marvel.ui.theme.getPrimaryColors
import com.reditum.marvel.ui.theme.isDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val heroes = MutableStateFlow<List<Character>>(emptyList())
    var errored = MutableStateFlow(false)

    private var offset = 0

    fun updateColors(isDark: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            heroes.value = heroes.value.map { char ->
                val color = char.colors!!.color
                char.copy(
                    colors = getPrimaryColors(color, isDark)
                )
            }
        }
    }

    fun load() {
        errored.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val isDark = context.resources.configuration.isDark()

            MarvelApi.getHeroes(offset)
                .onSuccess { res ->
                    res.map { char ->
                        val color = getNetworkImageColor(context, char.thumbnail.getUrl())
                        char.colors = getPrimaryColors(color, isDark)
                    }
                    heroes.update {
                        heroes.value + res
                    }
                    offset += REQUEST_LIMIT
                }.onFailure {
                    errored.value = true
                    Log.e("HomeViewModel", it.toString())
                }
        }
    }

    init {
        load()
    }
}