package com.zoothii

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zoothii.database.VideoGamesDao
import com.zoothii.models.Game
import com.zoothii.remote.VideoGamesApi
import com.zoothii.repositories.VideoGamesRepository
import com.zoothii.util.Constants.DEFAULT_START_PAGE
import com.zoothii.util.DataHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VideoGamesPagingSource(
    private val videoGamesApi: VideoGamesApi,
    private val videoGamesDao: VideoGamesDao,
    private val search: String?,
) : PagingSource<Int, Game>() {

    //@Inject lateinit var repository: VideoGamesRepository

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
                //Log.d("response games all", games.size.toString())

                videoGamesDao.addAllGames(games)

                if (!DataHolder.getInstance().gamesToRemove.isNullOrEmpty()) {
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
            //Log.d("response games removed", games.size.toString())

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