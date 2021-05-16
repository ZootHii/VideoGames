package com.zoothii.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.zoothii.VideoGamesPagingSource
import com.zoothii.database.VideoGamesDao
import com.zoothii.models.Game
import com.zoothii.models.PageResult
import com.zoothii.remote.VideoGamesApi
import retrofit2.Response

class VideoGamesRepository(
    private val api: VideoGamesApi,
    private val videoGamesDao: VideoGamesDao
) {

    suspend fun getGameDetails(id: Int): Response<Game> = api.getGameDetail(id)

    suspend fun getGames(page: Int?, search: String?, pageSize: Int?): Response<PageResult> =
        api.getGames(page, search, pageSize)

    fun getGamesPaging(search: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VideoGamesPagingSource(api, videoGamesDao, search) }
        ).liveData


    suspend fun addGame(game: Game) =
        videoGamesDao.addGame(game)

/*    suspend fun deleteLikedGameDetails(game: Game) =
        videoGamesDao.deleteLikedGameDetails(game)*/

    fun getAllFavoriteGames() = videoGamesDao.getAllFavoriteGames()
    fun getAllFavoriteGames2(searchGameName: String?) = videoGamesDao.getAllFavoriteGames2(searchGameName)

    suspend fun deleteNonFavoriteGames() = videoGamesDao.deleteNonFavoriteGames()

    fun checkIfGameIsInFavorites(gameId: Int) = videoGamesDao.checkIfGameIsInFavorites(gameId)

    //fun searchGamesByGameName(gameName: String) = videoGamesDao.searchGamesByGameName(gameName)



    fun getAllGames() = videoGamesDao.getAllGames()
}