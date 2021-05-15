package com.zoothii.di

import com.zoothii.remote.VideoGamesApi
import com.zoothii.remote.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideVideoGamesApi(): VideoGamesApi {
        val service: VideoGamesApi by lazy {
            RetrofitService.buildService(VideoGamesApi::class.java)
        }
        return service
    }
}
