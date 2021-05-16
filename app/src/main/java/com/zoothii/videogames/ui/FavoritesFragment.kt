package com.zoothii.videogames.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zoothii.adapters.LikedGamesAdapter
import com.zoothii.models.Game
import com.zoothii.util.Constants
import com.zoothii.util.Helper
import com.zoothii.util.Helper.Companion.isNetworkAvailable
import com.zoothii.util.Helper.Companion.makeSnackBar
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
        setSearchView()

        videoGamesViewModel.getAllGames().observe(viewLifecycleOwner) { game ->
            game.forEach {
                Log.d("response ALL GAMES", "ID:${it.id}- NAME:${it.name}- FAV:${it.favorite}- WEB:${it.website} ")
            }
        }
        //getAllFavoriteGames()
        //getAllFavoriteGames2()
        //getAllFavoriteGames3()

        videoGamesViewModel.favoriteGames
            .observe(viewLifecycleOwner, { games ->
                likedGamesAdapter.setGames(games)
                if (games.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }

    private fun getAllFavoriteGames() {
        videoGamesViewModel.getAllFavoriteGames()
            .observe(viewLifecycleOwner, { games ->
                likedGamesAdapter.setGames(games)
                if (games.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }

    private fun setRecyclerView(favoritesRecyclerView: RecyclerView, view: View) {
        recyclerView = favoritesRecyclerView.apply {
            likedGamesAdapter =
                LikedGamesAdapter { game ->
                    onClickItem(view, game)
                }
            adapter = likedGamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun onClickItem(view: View, game: Game) {
        if (isNetworkAvailable(requireContext())) {
            val action =
                FavoritesFragmentDirections.actionNavigationFavoritesToGameFragment(game.id)
            Navigation.findNavController(view).navigate(action)
        } else {
            makeSnackBar(view, getString(R.string.no_internet), true)
                .setAction("OK") {}
                .show()
        }
    }

/*    // TODO TEST
    private fun getAllFavoriteGames2() {
        videoGamesViewModel.getAllFavoriteGames2()
            .observe(viewLifecycleOwner, { games ->
                likedGamesAdapter.setGames(games)
                if (games.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }*/


    // TODO TEST
    private fun getAllFavoriteGames3() {
        videoGamesViewModel.favoriteGames
            .observe(viewLifecycleOwner, { games ->
                likedGamesAdapter.setGames(games)
                if (games.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }


    private fun setSearchView() {
        fragmentFavoritesBinding.favoritesSearchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    /*canScrollToTop = true
                    if (!newText.isNullOrBlank()) {
                        canScrollToTop = false
                    }
                    scrollToPositionTop()*/
                    searchBehaviour(newText)
                    return true
                }
            })
        }
    }

    private fun searchBehaviour(newText: String?) {
        if (!newText.isNullOrEmpty()) {
            if (newText.length >= Constants.DEFAULT_START_SEARCH_TEXT_LENGTH) {
                val searchText = "%$newText%"
                videoGamesViewModel.searchFavoriteGames(searchText)
            } else {
                videoGamesViewModel.searchFavoriteGames()
            }
        } else {
            videoGamesViewModel.searchFavoriteGames()
        }
    }



}


/*    // TODO TEST
    private fun getLikedGameDetails() {
        videoGamesViewModel.getAllGames()
            .observe(viewLifecycleOwner, { gameDetailsList ->
                likedGamesAdapter.setGames(gameDetailsList)
                if (gameDetailsList.isEmpty()) {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.VISIBLE
                }
            })
    }
    // TODO TEST
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
    // TODO TEST
    private fun onClickItem(view: View, game: Game) {
        if (isNetworkAvailable(requireContext())){
            val action = FavoritesFragmentDirections.actionNavigationFavoritesToGameFragment(game.id)
            Navigation.findNavController(view).navigate(action)
        }
        else{
            makeSnackBar(view, getString(R.string.no_internet), true)
                .setAction("OK") {}
                .show()
        }

    }*/
