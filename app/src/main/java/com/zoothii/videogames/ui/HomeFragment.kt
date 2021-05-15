package com.zoothii.videogames.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.zoothii.adapters.RecyclerViewAdapter
import com.zoothii.adapters.ViewPagerAdapter
import com.zoothii.models.Game
import com.zoothii.util.Constants.DEFAULT_VIEW_PAGER_PAGE
import com.zoothii.util.Constants.DEFAULT_VIEW_PAGER_PAGE_SIZE
import com.zoothii.util.DataHolder
import com.zoothii.util.Helper.Companion.viewVisibility
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var games: ArrayList<Game>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2
    private lateinit var circleIndicator3: CircleIndicator3


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeBinding = FragmentHomeBinding.bind(view)

        fragmentHomeBinding.apply {
            setViewPagerAndCircleIndicator(homeViewPager, homeCircleIndicator, view)
            setRecyclerView(homeRecyclerView, view)
        }
        setSearchView()

        videoGamesViewModel.getGames(page = DEFAULT_VIEW_PAGER_PAGE,
            pageSize = DEFAULT_VIEW_PAGER_PAGE_SIZE)
            .observe(viewLifecycleOwner, { gamesResult ->
                games = gamesResult.games as ArrayList<Game>
                DataHolder.getInstance().gamesToRemove = games
                viewPagerAdapter.setGames(games)
                circleIndicator3.setViewPager(viewPager2)
            })

        videoGamesViewModel.games.observe(viewLifecycleOwner) { pagingData ->
            recyclerViewAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        recyclerViewAdapter.addLoadStateListener { loadState ->
            fragmentHomeBinding.apply {
                homeNotFound.apply {
                    visibility =
                        if (recyclerViewAdapter.itemCount < 1 && loadState.source.refresh is LoadState.NotLoading) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }

                progressBarHome.apply {
                    visibility =
                        if (loadState.source.refresh is LoadState.Loading) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }
            }
        }
    }

    private fun setSearchView() {
        fragmentHomeBinding.homeSearchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    scrollToPositionTop()
                    searchBehaviour(newText)
                    return true
                }
            })
        }
    }

    private fun searchBehaviour(newText: String?) {
        if (!newText.isNullOrEmpty()) {
            if (newText.length >= 3) {
                videoGamesViewModel.searchGames(newText)
                viewVisibility(viewPager2, true)
                viewVisibility(circleIndicator3, true)
            } else {
                videoGamesViewModel.searchGames()
                viewVisibility(viewPager2, false)
                viewVisibility(circleIndicator3, false)
            }
        } else {
            videoGamesViewModel.searchGames()
            viewVisibility(viewPager2, true)
            viewVisibility(circleIndicator3, true)
        }
    }

    private fun scrollToPositionTop() {
        recyclerViewAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                fragmentHomeBinding.homeRecyclerView.scrollToPosition(0)
            }
        }
    }

    private fun setViewPagerAndCircleIndicator(
        homeViewPager: ViewPager2,
        homeCircleIndicator: CircleIndicator3,
        view: View,
    ) {
        viewPager2 = homeViewPager.apply {
            viewPagerAdapter = ViewPagerAdapter { game ->
                onClickItem(view, game)
            }
            adapter = viewPagerAdapter
        }
        circleIndicator3 = homeCircleIndicator
    }

    private fun setRecyclerView(homeRecyclerView: RecyclerView, view: View) {
        recyclerView = homeRecyclerView.apply {
            recyclerViewAdapter =
                RecyclerViewAdapter { game ->
                    Log.d("log game click", game.toString())
                    onClickItem(view, game)
                }
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun onClickItem(view: View, game: Game) {
        val action = HomeFragmentDirections.actionNavigationHomeToGameFragment(game.id)
        Navigation.findNavController(view).navigate(action)
    }
}