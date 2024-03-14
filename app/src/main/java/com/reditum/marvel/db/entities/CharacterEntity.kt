package com.reditum.marvel.db.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.reditum.marvel.ui.theme.PrimaryColors

@Entity(
    tableName = "character"
)
data class CharacterEntity @JvmOverloads constructor(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val thumbnailColor: Int,
    @Ignore var primaryColors: PrimaryColors? = null
)