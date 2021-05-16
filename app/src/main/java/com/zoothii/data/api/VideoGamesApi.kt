package com.zoothii.data.api

import com.zoothii.data.models.Game
import com.zoothii.data.models.PageResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoGamesApi {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int?,
        @Query("search") search: String?,
        @Query("page_size") pageSize: Int?,
    ): Response<PageResult>

    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") id: Int): Response<Game>
}