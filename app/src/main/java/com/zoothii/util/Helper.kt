package com.zoothii.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Helper {

    companion object {
        fun viewVisibility(view: View, isGone: Boolean) {
            val visibility = if (isGone) View.GONE else View.VISIBLE
            view.visibility = visibility
        }

        fun makeSnackBar(view: View, message: String, isLong: Boolean): Snackbar {
            val duration = if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
            return Snackbar.make(view, message, duration)
        }
    }
}