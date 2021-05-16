package com.zoothii.videogames.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zoothii.videogames.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    //private val videoGamesViewModel: VideoGamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }

//    override fun onStop() {
//        super.onStop()
//        videoGamesViewModel.deleteNonFavoriteGames() // delete data saved for search
//    }

    fun hideNavigationBar() {
        navView.visibility = View.GONE
    }

    fun showNavigationBar() {
        navView.visibility = View.VISIBLE
    }
}