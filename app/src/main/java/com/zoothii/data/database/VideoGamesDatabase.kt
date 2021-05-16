package com.zoothii.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zoothii.data.models.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class VideoGamesDatabase : RoomDatabase() {
    abstract fun likedGameDetailsDao(): VideoGamesDao
    companion object {
        @Volatile
        private var instance: VideoGamesDatabase? = null

        fun getDatabase(context: Context): VideoGamesDatabase {
            val temp = instance
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoGamesDatabase::class.java,
                    "video_games_database"
                ).build()
                return instance as VideoGamesDatabase
            }
        }
    }
}
