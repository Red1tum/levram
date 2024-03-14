package com.reditum.marvel.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reditum.marvel.db.CharacterDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(
    db: CharacterDatabase,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val heroId = savedStateHandle.get<String>("id")!!.toInt()

    // removed load from network because the info
    // we get from characters endpoint is identical to characters/id
    // and if user sees character, then we already have his info in db
    val hero = db.character(heroId)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}