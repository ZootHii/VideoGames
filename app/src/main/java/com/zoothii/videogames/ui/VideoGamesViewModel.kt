package com.zoothii.videogames.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zoothii.models.GameDetails
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

    fun getGameDetails(id: Int): MutableLiveData<GameDetails> {
        val mutableLiveData = MutableLiveData<GameDetails>()
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

    fun addLikedGameDetails(gameDetails: GameDetails) {
        viewModelScope.launch {
            repository.addLikedGameDetails(gameDetails)
        }
    }

    fun deleteLikedGameDetails(gameDetails: GameDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLikedGameDetails(gameDetails)
        }
    }

    fun getLikedGameDetails() = repository.getAllLikedGameDetails()


    private val currentSearch = MutableLiveData(DEFAULT_SEARCH_TEXT)

    val games = currentSearch.switchMap { searchString ->
        repository.getGamesPaging(searchString).cachedIn(viewModelScope)
    }

    fun searchGames(search: String? = DEFAULT_SEARCH_TEXT) {
        currentSearch.value = search
    }
}