package com.reditum.marvel.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.reditum.marvel.db.entities.CharacterEntity
import com.reditum.marvel.db.entities.OffsetEntity

class CharacterDatabase(
    private val delegate: InternalDatabase
): CharacterDao by delegate.characterDao() {
    val openHelper: SupportSQLiteOpenHelper
        get() = delegate.openHelper

    fun query(block: CharacterDatabase.() -> Unit) = with(delegate) {
        queryExecutor.execute {
            block(this@CharacterDatabase)
        }
    }

    fun transaction(block: CharacterDatabase.() -> Unit) = with(delegate) {
        transactionExecutor.execute {
            runInTransaction {
                block(this@CharacterDatabase)
            }
        }
    }

    fun close() = delegate.close()
}

@Database(
    entities = [CharacterEntity::class, OffsetEntity::class],
    version = 1,
    exportSchema = true
)
abstract class InternalDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        private const val DB_NAME = "character.db"

        fun newInstance(context: Context): CharacterDatabase {
            return CharacterDatabase(
                Room.databaseBuilder(context, InternalDatabase::class.java, DB_NAME)
                    .build()
            )
        }
    }
}