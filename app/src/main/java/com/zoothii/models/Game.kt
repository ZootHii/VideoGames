package com.zoothii.models

import com.google.gson.annotations.SerializedName

open class Game(

    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("released")
    val released: String,
    @SerializedName("background_image")
    val backgroundImage: String,
    @SerializedName("metacritic")
    val metacritic: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
)