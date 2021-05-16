package com.zoothii.videogames.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zoothii.models.Game
import com.zoothii.models.PageResult
import com.zoothii.repositories.VideoGamesRepository
import com.zoothii.util.Constants.DEFAULT_SEARCH_TEXT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoGamesViewModel @Inject constructor(private val repository: VideoGamesRepository) :
    ViewModel() {

    fun getGames(
        page: Int? = null,
        search: String? = null,
        pageSize: Int? = null,
    ): MutableLiveData<PageResult> {
        val mutableLiveData = MutableLiveData<PageResult>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getGames(page, search, pageSize)
            if (response.isSuccessful) {
                response.body()?.let {
                    mutableLiveData.postValue(response.body())
                }
            } else {
                Log.d("Error", "error")
            }
        }
        return mutableLiveData
    }

    fun getGameDetails(id: Int): MutableLiveData<Game> {
        val mutableLiveData = MutableLiveData<Game>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getGameDetails(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    mutableLiveData.postValue(response.body())
                }
            } else {
                Log.d("Error", "error")
            }
        }
        return mutableLiveData
    }

    fun addGame(game: Game) {
        viewModelScope.launch {
            repository.addGame(game)
        }
    }

    //fun getAllFavoriteGames() = repository.getAllFavoriteGames()
    //fun getAllFavoriteGames2(searchGameName: String? = "%%") = repository.getAllFavoriteGames2(searchGameName)


    fun getAllGames() = repository.getAllGames()
    fun checkIfGameIsInFavorites(gameId: Int) = repository.checkIfGameIsInFavorites(gameId)
    fun deleteNonFavoriteGames() = viewModelScope.launch { repository.deleteNonFavoriteGames() }



    // Search games from api
    private val currentSearch = MutableLiveData(DEFAULT_SEARCH_TEXT)
    val games = currentSearch.switchMap { searchString ->
        repository.getGamesPaging(searchString).cachedIn(viewModelScope)
    }
    fun searchGames(search: String? = DEFAULT_SEARCH_TEXT) {
        currentSearch.value = search
    }

    // Search favorites from database room
    private val currentSearchDao = MutableLiveData("%%")
    val favoriteGames = currentSearchDao.switchMap { searchString ->
        repository.getAllFavoriteGames2(searchString)
    }
    fun searchFavoriteGames(search: String? = "%%") {
        currentSearchDao.value = search
    }

}