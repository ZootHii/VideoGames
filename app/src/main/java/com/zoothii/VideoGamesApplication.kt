package com.zoothii

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VideoGamesApplication : Application() {
    // TODO needed for dependency injection viewModels in fragments
    // TODO add 'android:name=".VideoGamesApplication"' inside AndroidManifest
}