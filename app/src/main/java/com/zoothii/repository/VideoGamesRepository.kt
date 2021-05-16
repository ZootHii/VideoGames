package com.zoothii.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.zoothii.data.api.VideoGamesApi
import com.zoothii.data.database.VideoGamesDao
import com.zoothii.data.models.Game
import com.zoothii.data.models.PageResult
import com.zoothii.repository.paging.VideoGamesPagingSource
import retrofit2.Response

class VideoGamesRepository(
    private val api: VideoGamesApi,
    private val videoGamesDao: VideoGamesDao,
) {
    // api s
    fun getGamesPagingApi(search: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VideoGamesPagingSource(api, videoGamesDao, search) }
        ).liveData

    suspend fun getGamesApi(page: Int?, search: String?, pageSize: Int?): Response<PageResult> =
        api.getGames(page, search, pageSize)

    suspend fun getGameDetails(id: Int): Response<Game> = api.getGameDetail(id)


    // dao s
    suspend fun addGame(game: Game) = videoGamesDao.addGame(game)

    suspend fun deleteNonFavoriteGames() = videoGamesDao.deleteNonFavoriteGames()

    fun getGamesDatabase() = videoGamesDao.getGames()

    fun getFavoriteGames(searchGameName: String?) = videoGamesDao.getFavoriteGames(searchGameName)

    fun isFavoriteGame(gameId: Int) = videoGamesDao.isFavoriteGame(gameId)
}
