package com.zoothii.di

import android.content.Context
import com.zoothii.data.database.VideoGamesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideVideoGamesDatabase(@ApplicationContext context: Context): VideoGamesDatabase {
        return VideoGamesDatabase.getDatabase(context)
    }
}