package com.reditum.marvel.db.entities

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.reditum.marvel.data.Character
import com.reditum.marvel.ui.theme.PrimaryColors
import com.reditum.marvel.ui.theme.getNetworkImageColor

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

// fun was really suspended after context
// was passed to suspend map function
suspend fun Character.toEntity(context: Context) = CharacterEntity(
    id,
    name,
    description,
    thumbnail.getUrl(),
    getNetworkImageColor(context, thumbnail.getUrl()).toArgb()
)