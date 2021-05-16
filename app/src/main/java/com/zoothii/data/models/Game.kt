package com.zoothii.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "games")
data class Game(
    @PrimaryKey
    val id: Int,
    val name: String,
    val released: String?,
    val metacritic: Int?,
    val rating: Double?,
    @SerializedName("background_image")
    val backgroundImage: String?,
    val description: String?,
    val website: String?,
    @ColumnInfo(name = "favorite")
    var favorite: Int = 0,
) : Parcelable