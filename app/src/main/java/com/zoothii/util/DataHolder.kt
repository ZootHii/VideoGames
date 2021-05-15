package com.zoothii.util

import com.zoothii.models.Game


class DataHolder {

    companion object {
        @Volatile
        private var instance: DataHolder? = null

/*        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DataHolder().also { instance = it }
            }*/

        fun getInstance(): DataHolder {
            val temp = DataHolder.instance
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                instance = DataHolder()
                return instance as DataHolder
            }
        }


    }

    var isSearching: Boolean = false
    var gamesToRemove: ArrayList<Game> = ArrayList<Game>()


}