package com.zoothii.videogames.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.zoothii.models.Game
import com.zoothii.util.Helper.Companion.makeSnackBar
import com.zoothii.videogames.MainActivity
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameFragment :
    Fragment(R.layout.fragment_game) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()
    private lateinit var fragmentGameBinding: FragmentGameBinding
    private var gameId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //retainInstance = true
        (requireActivity() as MainActivity).hideNavigationBar()
        fragmentGameBinding = FragmentGameBinding.bind(view)
        arguments?.let {
            gameId = it.getInt("game_id")
        }

        fragmentGameBinding.progressBarGame.visibility = View.VISIBLE
        videoGamesViewModel.getGameDetails(gameId).observe(viewLifecycleOwner, { gameDetails ->
            fragmentGameBinding.apply {
                val releasedAt = "Released at ${gameDetails.released}"
                val metaCriticRate = "Metacritic Rate ${gameDetails.metacritic}"
                imageGameBackground.load(gameDetails.backgroundImage)
                gameName.text = gameDetails.name
                gameReleased.text = releasedAt
                gameMetaCritic.text = metaCriticRate
                gameDescription.text =
                    gameDetails.description?.let {
                        HtmlCompat.fromHtml(
                            it,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    }
                gameWebsite.apply {
                    text = gameDetails.website
                    setOnClickListener {
                        gameDetails.website?.let { it1 -> onClickWebsite(it1) }
                    }
                }
                progressBarGame.visibility = View.GONE
                gameAddLikedButton.apply {
                    visibility = View.VISIBLE
                    buttonBehaviour(gameDetails)
                }
            }
        })
        navigationBarControlWithBackButton()
    }

    private fun buttonBehaviour(game: Game) {
        videoGamesViewModel.checkIfGameIsInFavorites(gameId)
            .observe(viewLifecycleOwner, { gameList ->
                if (gameList.isNullOrEmpty() || gameList[0].id != game.id) {
                    fragmentGameBinding.apply {
                        gameAddLikedButton.apply {
                            setImageResource(R.drawable.ic_add_favorite)
                            setOnClickListener {
                                //addLikedGameDetails(game)
                                addFavorite(game)
                                val message = "${game.name} added to favorites."
                                makeSnackBar(root, message, false)
                                    .setAction("OK") {}
                                    .show()
                            }
                        }
                    }
                } else {
                    fragmentGameBinding.apply {
                        gameAddLikedButton.apply {
                            setImageResource(R.drawable.ic_delete)
                            setOnClickListener {
                                //deleteLikedGameDetails(game)
                                deleteFavorite(game)
                                val message = "${game.name} removed from favorites."
                                makeSnackBar(root, message, false)
                                    .setAction("OK") {}
                                    .show()
                            }
                        }
                    }
                }
            })
    }

    private fun onClickWebsite(website: String) {
        var site = website
        if (!site.startsWith("http://") && !site.startsWith("https://")) {
            site = "http://$website";
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(site))
        startActivity(browserIntent)
    }

    private fun navigationBarControlWithBackButton() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (requireActivity() as MainActivity).showNavigationBar()
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    private fun addFavorite(game: Game) {
        game.favorite = 1
        videoGamesViewModel.addGame(game)
    }

    private fun deleteFavorite(game: Game) {
        game.favorite = 0
        videoGamesViewModel.addGame(game)
    }
}


/*    private fun deleteLikedGameDetails(game: Game) {
        videoGamesViewModel.deleteLikedGameDetails(game)
    }*/

/*    private fun checkIfGameIsInFavorites(gameId: Int) {
        videoGamesViewModel.checkIfGameIsInFavorites(gameId).observe(viewLifecycleOwner, {
            //Log.d("response check if size", it.size.toString())
        })
    }*/


/*    private fun addLikedGameDetails(game: Game) {
        videoGamesViewModel.addGameToFavorites(game)
    }*/