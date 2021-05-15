package com.zoothii.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zoothii.models.Game
import com.zoothii.models.GameDetails

@Dao
interface LikedGameDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedGameDetails(gameDetails: GameDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllGames(games: List<Game>)

    @Delete
    suspend fun deleteLikedGameDetails(gameDetails: GameDetails)

    @Query("SELECT * FROM liked_game_details")
    fun getAllLikedGameDetails(): LiveData<List<GameDetails>>

    @Query("SELECT * FROM games")
    fun getAllGames(): LiveData<List<Game>>
}