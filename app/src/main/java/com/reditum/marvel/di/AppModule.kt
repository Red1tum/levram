package com.reditum.marvel.di

import android.content.Context
import com.reditum.marvel.db.CharacterDatabase
import com.reditum.marvel.db.InternalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CharacterDatabase =
        InternalDatabase.newInstance(context)
}