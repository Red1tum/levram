package com.reditum.marvel.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.data.Character
import com.reditum.marvel.data.MarvelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HeroViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val hero = MutableStateFlow<Character?>(null)
    var errored by mutableStateOf(false)

    val heroId = savedStateHandle.get<String>("id")!!.toInt()

    fun load() {
        errored = false
        viewModelScope.launch(Dispatchers.IO) {
            MarvelApi.getHero(heroId)
                .onSuccess { char ->
                    hero.value = char
                }.onFailure {
                    errored = true
                }
        }
    }

    init {
        load()
    }
}