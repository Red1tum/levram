package com.reditum.marvel.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "offset"
)
data class OffsetEntity(
    @PrimaryKey(autoGenerate = true) val idx: Int = 1,
    var value: Int
)