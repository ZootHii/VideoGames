package com.zoothii.videogames.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.zoothii.data.models.Game
import com.zoothii.util.Helper.Companion.makeSnackBar
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentGameBinding
import com.zoothii.videogames.ui.VideoGamesViewModel
import com.zoothii.videogames.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameFragment :
    Fragment(R.layout.fragment_game) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()
    private lateinit var fragmentGameBinding: FragmentGameBinding
    private var gameId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideNavigationBar()

        fragmentGameBinding = FragmentGameBinding.bind(view)

        arguments?.let {
            gameId = it.getInt("game_id")
        }

        fragmentGameBinding.progressBarGame.visibility = View.VISIBLE
        setGameDetails()
        navigationBarControlWithBackButton()
    }

    private fun setGameDetails() {
        videoGamesViewModel.getGameDetails(gameId).observe(viewLifecycleOwner) { gameDetails ->
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
        }
    }

    private fun buttonBehaviour(game: Game) {
        videoGamesViewModel.isFavoriteGame(gameId)
            .observe(viewLifecycleOwner) { gameList ->
                if (gameList.isNullOrEmpty() || gameList[0].id != game.id) {
                    fragmentGameBinding.apply {
                        gameAddLikedButton.apply {
                            setImageResource(R.drawable.ic_add_favorite)
                            setOnClickListener {
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
                                deleteFavorite(game)
                                val message = "${game.name} removed from favorites."
                                makeSnackBar(root, message, false)
                                    .setAction("OK") {}
                                    .show()
                            }
                        }
                    }
                }
            }
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