package com.zoothii.models


import com.google.gson.annotations.SerializedName
import com.zoothii.models.Game

data class PageResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val games: List<Game>,
)