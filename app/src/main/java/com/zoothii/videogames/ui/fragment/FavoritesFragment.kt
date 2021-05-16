package com.zoothii.videogames.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zoothii.adapters.FavoriteGamesAdapter
import com.zoothii.data.models.Game
import com.zoothii.util.Constants
import com.zoothii.util.Helper.Companion.isNetworkAvailable
import com.zoothii.util.Helper.Companion.makeSnackBar
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentFavoritesBinding
import com.zoothii.videogames.ui.VideoGamesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()

    private lateinit var fragmentFavoritesBinding: FragmentFavoritesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteGamesAdapter: FavoriteGamesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFavoritesBinding = FragmentFavoritesBinding.bind(view)

        fragmentFavoritesBinding.apply {
            setRecyclerView(favoritesRecyclerView, view)
        }

//        videoGamesViewModel.getGamesDatabase().observe(viewLifecycleOwner) { game ->
//            Log.d("response size all games added from page", game.size.toString())
//            /*game.forEach {
//                Log.d("response ALL GAMES",
//                    "ID:${it.id}- NAME:${it.name}- FAV:${it.favorite}- WEB:${it.website} ")
//            }*/
//        }
        setSearchView()

        getFavoriteGames()
    }

    private fun setRecyclerView(favoritesRecyclerView: RecyclerView, view: View) {
        recyclerView = favoritesRecyclerView.apply {
            favoriteGamesAdapter =
                FavoriteGamesAdapter { game ->
                    onClickItem(view, game)
                }
            adapter = favoriteGamesAdapter
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

    private fun getFavoriteGames() {
        videoGamesViewModel.favoriteGames
            .observe(viewLifecycleOwner) { games ->
                favoriteGamesAdapter.setGames(games)
                if (games.isEmpty()) {
                    videoGamesViewModel.allFavoriteGames.observe(viewLifecycleOwner) {
                        if (it.isEmpty()) {
                            fragmentFavoritesBinding.favoritesNotFound.apply {
                                text = getString(R.string.favorites_empty)
                                visibility = View.VISIBLE
                            }
                        } else {
                            fragmentFavoritesBinding.favoritesNotFound.apply {
                                text = getString(R.string.favorites_not_found)
                                visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    fragmentFavoritesBinding.favoritesNotFound.visibility = View.GONE
                }
            }
    }

    private fun setSearchView() {
        fragmentFavoritesBinding.favoritesSearchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    fragmentFavoritesBinding.favoritesRecyclerView.scrollToPosition(0)
                    searchBehaviour(newText)
                    return true
                }
            })
        }
    }

    private fun searchBehaviour(newText: String?) {
        if (!newText.isNullOrEmpty()) {
            if (newText.length >= Constants.DEFAULT_START_SEARCH_TEXT_LENGTH) {
                videoGamesViewModel.searchFavoriteGames(newText)
            } else {
                videoGamesViewModel.searchFavoriteGames()
            }
        } else {
            videoGamesViewModel.searchFavoriteGames()
        }
    }
}
