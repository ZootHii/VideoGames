package com.zoothii.repository.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zoothii.data.api.VideoGamesApi
import com.zoothii.data.database.VideoGamesDao
import com.zoothii.data.models.Game
import com.zoothii.util.Constants.DEFAULT_START_PAGE
import com.zoothii.util.DataHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class VideoGamesPagingSource(
    private val videoGamesApi: VideoGamesApi,
    private val videoGamesDao: VideoGamesDao,
    private val search: String?,
) : PagingSource<Int, Game>() {

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val position = params.key ?: DEFAULT_START_PAGE

        return try {
            val response = videoGamesApi.getGames(position, search, params.loadSize)
            var games = arrayListOf<Game>()
            if (response.isSuccessful) {
                games = response.body()?.games as ArrayList<Game>

                if (!DataHolder.getInstance().gamesToRemove.isNullOrEmpty()) { // remove viewpager games
                    val gamesToRemove = DataHolder.getInstance().gamesToRemove
                    gamesToRemove.forEach { gameToRemove ->
                        games.removeIf { game ->
                            game.id == gameToRemove.id
                        }
                    }
                }
            } else {
                Log.d("Error", "error")
            }

            //videoGamesDao.addAllGames(games) // adds every page update to database dao

            LoadResult.Page(
                data = games,
                prevKey = if (position == DEFAULT_START_PAGE) null else position - 1,
                nextKey = if (games.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}