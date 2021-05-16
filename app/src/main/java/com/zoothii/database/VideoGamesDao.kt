package com.zoothii.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.zoothii.models.Game

@Dao
interface VideoGamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGame(game: Game)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllGames(games: List<Game>)

/*    @Delete
    suspend fun deleteLikedGameDetails(game: Game)*/

    @Query("DELETE FROM games WHERE favorite = 0")
    suspend fun deleteNonFavoriteGames()

/*    //@Query("SELECT * FROM games WHERE name LIKE '%'||:gameName||'%'")
    @Query("SELECT * FROM games WHERE name = :gameName")
    fun searchGamesByGameName(gameName: String? = ""): LiveData<List<Game>>*/

/*    @Query("SELECT * FROM liked_games")
    fun getAllFavoriteGames(): LiveData<List<Game>>*/

    @Query("SELECT * FROM games WHERE id = :gameId AND favorite = 1")
    fun checkIfGameIsInFavorites(gameId: Int): LiveData<List<Game>>

    @Query("SELECT * FROM games ORDER BY name DESC")
    fun getAllGames(): LiveData<List<Game>>

    @Query("SELECT * FROM games WHERE favorite = 1 ORDER BY name DESC")
    fun getAllFavoriteGames(): LiveData<List<Game>>

    @Query("SELECT * FROM games WHERE favorite = 1 AND name LIKE :searchGameName ORDER BY name DESC")
    fun getAllFavoriteGames2(searchGameName: String?): LiveData<List<Game>>

}