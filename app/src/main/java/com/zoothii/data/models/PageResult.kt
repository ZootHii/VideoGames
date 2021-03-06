package com.zoothii.data.models


import com.google.gson.annotations.SerializedName

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