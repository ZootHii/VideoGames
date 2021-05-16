package com.zoothii.di

import com.zoothii.data.database.VideoGamesDao
import com.zoothii.data.database.VideoGamesDatabase
import com.zoothii.data.api.VideoGamesApi
import com.zoothii.repository.VideoGamesRepository
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
