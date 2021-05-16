package com.zoothii.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zoothii.data.models.Game

@Dao
interface VideoGamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGame(game: Game)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // for search in room adding all data comes from pagingSource
    suspend fun addAllGames(games: List<Game>)

    @Query("DELETE FROM games WHERE favorite = 0")
    suspend fun deleteNonFavoriteGames()

    @Query("SELECT * FROM games WHERE id = :gameId AND favorite = 1")
    fun isFavoriteGame(gameId: Int): LiveData<List<Game>>

    @Query("SELECT * FROM games ORDER BY name DESC")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT * FROM games WHERE favorite = 1 AND REPLACE(name, ' ', '') LIKE REPLACE('%'||:searchGameName||'%', ' ', '') ORDER BY name DESC")
    fun getFavoriteGames(searchGameName: String?): LiveData<List<Game>>
}