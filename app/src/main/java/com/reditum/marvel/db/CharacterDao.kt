package com.reditum.marvel.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.reditum.marvel.db.entities.CharacterEntity
import com.reditum.marvel.db.entities.OffsetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT EXISTS (SELECT 1 FROM character)")
    fun isCharactersNotEmpty(): Int

    @Query("SELECT * FROM character ORDER BY name")
    fun characters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM character ORDER BY name")
    fun characterList(): List<CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :id")
    fun character(id: Int): CharacterEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM `offset`)")
    fun isOffsetNotEmpty(): Int

    @Query("SELECT * FROM `offset` WHERE idx = 1")
    fun offset(): OffsetEntity

    @Upsert
    fun upsert(character: CharacterEntity)

    @Upsert
    fun upsert(offset: OffsetEntity)

    @Delete
    fun delete(character: CharacterEntity)
}