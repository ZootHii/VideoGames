package com.zoothii.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zoothii.models.GameDetails

@Dao
interface LikedGameDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLikedGameDetails(gameDetails: GameDetails)

    @Delete
    suspend fun deleteLikedGameDetails(gameDetails: GameDetails)

    @Query("SELECT * FROM liked_game_details")
    fun getAllLikedGameDetails(): LiveData<List<GameDetails>>
}