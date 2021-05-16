package com.zoothii.di

import com.zoothii.data.api.VideoGamesApi
import com.zoothii.data.api.VideoGamesRetrofit
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
            VideoGamesRetrofit.buildApi(VideoGamesApi::class.java)
        }
        return service
    }
}
