package com.zoothii

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zoothii.models.Game
import com.zoothii.remote.VideoGamesApi
import com.zoothii.util.DataHolder
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE = 1

class VideoGamesPagingSource(
    private val videoGamesApi: VideoGamesApi,
    private val search: String?,
) : PagingSource<Int, Game>() {
    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val position = params.key ?: START_PAGE

        return try {
            val response = videoGamesApi.getGames(position, search, params.loadSize)
            var games = arrayListOf<Game>()

            if (response.isSuccessful) {
                games = response.body()?.games as ArrayList<Game>

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

            LoadResult.Page(
                data = games,
                prevKey = if (position == START_PAGE) null else position - 1,
                nextKey = if (games.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}