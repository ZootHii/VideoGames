package com.zoothii.di

import com.zoothii.database.VideoGamesDao
import com.zoothii.database.VideoGamesDatabase
import com.zoothii.remote.VideoGamesApi
import com.zoothii.repositories.VideoGamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideVideoGamesRepository(
        videoGamesApi: VideoGamesApi,
        videoGamesDao: VideoGamesDao
    ): VideoGamesRepository {
        return VideoGamesRepository(videoGamesApi, videoGamesDao)
    }

    @Singleton
    @Provides
    fun provideLikedGameDetailsDao(videoGamesDatabase: VideoGamesDatabase) =
        videoGamesDatabase.likedGameDetailsDao()

}
