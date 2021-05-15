package com.zoothii.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "liked_game_details")
data class GameDetails(
    @PrimaryKey
    val id: Int,
    val name: String,
    val released: String,
    val description: String,
    @SerializedName("background_image")
    val backgroundImage: String,
    val rating: Double,
    val metacritic: Int,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
    val website: String,
)