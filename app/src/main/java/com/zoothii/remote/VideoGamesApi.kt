package com.zoothii.remote

import com.zoothii.models.Game
import com.zoothii.models.PageResult
import com.zoothii.util.Constants.DEFAULT_PAGE
import com.zoothii.util.Constants.DEFAULT_PAGE_SIZE
import com.zoothii.util.Constants.DEFAULT_SEARCH_TEXT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoGamesApi {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int? = DEFAULT_PAGE,
        @Query("search") search: String? = DEFAULT_SEARCH_TEXT,
        @Query("page_size") pageSize: Int? = DEFAULT_PAGE_SIZE,
    ): Response<PageResult>

    @GET("games/{id}")
    suspend fun getGameDetail(@Path("id") id: Int): Response<Game>

}