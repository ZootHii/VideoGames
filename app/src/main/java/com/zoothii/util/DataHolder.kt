package com.zoothii.util

import com.zoothii.data.models.Game

class DataHolder {

    companion object {
        @Volatile
        private var instance: DataHolder? = null

        fun getInstance(): DataHolder {
            val temp = instance
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                instance = DataHolder()
                return instance as DataHolder
            }
        }
    }

    var gamesToRemove = arrayListOf<Game>() // for view pager first items
}