package com.zoothii.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "games")
open class Game(

    @PrimaryKey
    val id: Int,
    val name: String,
    val released: String,
    @SerializedName("background_image")
    val backgroundImage: String,
    val metacritic: Int,
    val rating: Double,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
)