package com.zoothii.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.zoothii.VideoGamesPagingSource
import com.zoothii.database.LikedGameDetailsDao
import com.zoothii.models.GameDetails
import com.zoothii.models.PageResult
import com.zoothii.remote.VideoGamesApi
import retrofit2.Response

class VideoGamesRepository(
    private val api: VideoGamesApi,
    private val likedGameDetailsDao: LikedGameDetailsDao
) {

    suspend fun getGameDetails(id: Int): Response<GameDetails> = api.getGameDetail(id)

    suspend fun getGames(page: Int?, search: String?, pageSize: Int?): Response<PageResult> =
        api.getGames(page, search, pageSize)

    fun getGamesPaging(search: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VideoGamesPagingSource(api, likedGameDetailsDao, search) }
        ).liveData


    suspend fun addLikedGameDetails(gameDetails: GameDetails) =
        likedGameDetailsDao.addLikedGameDetails(gameDetails)

    suspend fun deleteLikedGameDetails(gameDetails: GameDetails) =
        likedGameDetailsDao.deleteLikedGameDetails(gameDetails)

    fun getAllLikedGameDetails() = likedGameDetailsDao.getAllLikedGameDetails()

    fun getAllGames() = likedGameDetailsDao.getAllGames()
}