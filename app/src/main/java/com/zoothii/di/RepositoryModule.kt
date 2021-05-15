package com.zoothii.di

import com.zoothii.database.LikedGameDetailsDao
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
        likedGameDetailsDao: LikedGameDetailsDao
    ): VideoGamesRepository {
        return VideoGamesRepository(videoGamesApi, likedGameDetailsDao)
    }

    @Singleton
    @Provides
    fun provideLikedGameDetailsDao(videoGamesDatabase: VideoGamesDatabase) =
        videoGamesDatabase.likedGameDetailsDao()

}
