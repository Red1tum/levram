package com.reditum.marvel.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.data.Character
import com.reditum.marvel.data.MarvelApi
import com.reditum.marvel.ui.theme.getNetworkImageColor
import com.reditum.marvel.ui.theme.getPrimaryColors
import com.reditum.marvel.ui.theme.isDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HeroViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val hero = MutableStateFlow<Character?>(null)
    var errored by mutableStateOf(false)

    val heroId = savedStateHandle.get<String>("id")!!.toInt()

    fun load() {
        errored = false
        viewModelScope.launch(Dispatchers.IO) {
            MarvelApi.getHero(heroId)
                .onSuccess { char ->
                    val context = getApplication<Application>().applicationContext
                    val isDark = context.resources.configuration.isDark()
                    hero.value = char.apply {
                        val color = getNetworkImageColor(context, char.thumbnail.getUrl())
                        colors = getPrimaryColors(color, isDark)
                    }
                }.onFailure {
                    errored = true
                }
        }
    }

    init {
        load()
    }
}