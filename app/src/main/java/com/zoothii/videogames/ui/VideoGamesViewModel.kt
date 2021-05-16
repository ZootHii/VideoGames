package com.zoothii.videogames.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.firebase.analytics.ktx.logEvent
import com.zoothii.data.models.Game
import com.zoothii.data.models.PageResult
import com.zoothii.repository.VideoGamesRepository
import com.zoothii.util.Constants.DEFAULT_PAGE
import com.zoothii.util.Constants.DEFAULT_PAGE_SIZE
import com.zoothii.util.Constants.DEFAULT_SEARCH_TEXT
import com.zoothii.util.Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoGamesViewModel @Inject constructor(private val repository: VideoGamesRepository) :
    ViewModel() {

    fun getGamesApi(
        page: Int? = DEFAULT_PAGE,
        search: String? = DEFAULT_SEARCH_TEXT,
        pageSize: Int? = DEFAULT_PAGE_SIZE,
    ): MutableLiveData<PageResult> {
        val mutableLiveData = MutableLiveData<PageResult>()
        viewModelScope.launch {
            val response = repository.getGamesApi(page, search, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { pageResult ->
                    mutableLiveData.postValue(pageResult)
                }
            } else {
                Log.d("Error", "error")
            }
        }
        return mutableLiveData
    }

    fun getGameDetails(id: Int): MutableLiveData<Game> {
        val mutableLiveData = MutableLiveData<Game>()
        val bundle = Bundle()
        viewModelScope.launch {
            val response = repository.getGameDetails(id)
            if (response.isSuccessful) {
                response.body()?.let { game ->
                    bundle.putParcelable("game_get_game_details", game)
                    Helper.firebaseAnalytics.logEvent("open_game_detail"){
                        //param("gameId", game.id.toLong())
                        param("game_get_game_details", bundle)
                    }

                    mutableLiveData.postValue(game)
                }
            } else {
                Log.d("Error", "error")
            }
        }
        return mutableLiveData
    }

    fun addGame(game: Game) {
        val bundle = Bundle()
        bundle.putParcelable("game_add_game", game)
        if (game.favorite == 1){
            Helper.firebaseAnalytics.logEvent("add_favorite"){
                //param("gameId", game.id.toLong())
                param("game_add_game", bundle)
            }
        }
        else {
            Helper.firebaseAnalytics.logEvent("delete_favorite"){
                //param("gameId", game.id.toLong())
                param("game_add_game", bundle)
            }
        }

        viewModelScope.launch {
            repository.addGame(game)
        }
    }

    //fun getAllFavoriteGames() = repository.getAllFavoriteGames()
    //fun getAllFavoriteGames2(searchGameName: String? = "%%") = repository.getAllFavoriteGames2(searchGameName)


    fun getGamesDatabase() = repository.getGamesDatabase()

    //val gamesDatabase2 = repository.getGamesDatabase()

    fun isFavoriteGame(gameId: Int) = repository.isFavoriteGame(gameId)

    fun deleteNonFavoriteGames() = viewModelScope.launch { repository.deleteNonFavoriteGames() }

    //val deleteNonFavorites = viewModelScope.launch { repository.deleteNonFavoriteGames() }


    // Search games from api
    private val currentSearch = MutableLiveData(DEFAULT_SEARCH_TEXT)
    val games = currentSearch.switchMap { searchString ->
        repository.getGamesPagingApi(searchString).cachedIn(viewModelScope)
    }

    fun searchGames(search: String? = DEFAULT_SEARCH_TEXT) {
        currentSearch.value = search
    }

    val allFavoriteGames = repository.getFavoriteGames(DEFAULT_SEARCH_TEXT)

    // Search favorites from database room
    private val currentSearchDao = MutableLiveData(DEFAULT_SEARCH_TEXT)
    val favoriteGames = currentSearchDao.switchMap { searchString ->
        repository.getFavoriteGames(searchString)
    }

    fun searchFavoriteGames(search: String? = DEFAULT_SEARCH_TEXT) {
        currentSearchDao.value = search
    }
}