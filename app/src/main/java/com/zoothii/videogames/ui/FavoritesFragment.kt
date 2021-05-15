package com.zoothii.videogames.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zoothii.adapters.LikedGamesAdapter
import com.zoothii.models.GameDetails
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()

    private lateinit var fragmentFavoritesBinding: FragmentFavoritesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var likedGamesAdapter: LikedGamesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFavoritesBinding = FragmentFavoritesBinding.bind(view)

        fragmentFavoritesBinding.apply {
            setRecyclerView(favoritesRecyclerView, view)
        }

        getLikedGameDetails()
    }

    private fun getLikedGameDetails() {
        videoGamesViewModel.getLikedGameDetails()
            .observe(viewLifecycleOwner, { gameDetailsList ->
                likedGamesAdapter.setGames(gameDetailsList)
                if (gameDetailsList.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }

    private fun setRecyclerView(favoritesRecyclerView: RecyclerView, view: View) {
        recyclerView = favoritesRecyclerView.apply {
            likedGamesAdapter =
                LikedGamesAdapter { gameDetails ->
                    onClickItem(view, gameDetails)
                }
            adapter = likedGamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun onClickItem(view: View, game: GameDetails) {
        val action = FavoritesFragmentDirections.actionNavigationFavoritesToGameFragment(game.id)
        Navigation.findNavController(view).navigate(action)
    }
}