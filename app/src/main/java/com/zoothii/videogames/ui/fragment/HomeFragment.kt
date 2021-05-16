package com.zoothii.videogames.ui.fragment

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
import com.zoothii.adapters.GamesAdapter
import com.zoothii.adapters.ViewPagerAdapter
import com.zoothii.data.models.Game
import com.zoothii.util.Constants.DEFAULT_START_SEARCH_TEXT_LENGTH
import com.zoothii.util.Constants.DEFAULT_VIEW_PAGER_PAGE
import com.zoothii.util.Constants.DEFAULT_VIEW_PAGER_PAGE_SIZE
import com.zoothii.util.DataHolder
import com.zoothii.util.Helper
import com.zoothii.util.Helper.Companion.viewVisibility
import com.zoothii.videogames.R
import com.zoothii.videogames.databinding.FragmentHomeBinding
import com.zoothii.videogames.ui.VideoGamesViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val videoGamesViewModel: VideoGamesViewModel by viewModels()

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var games: ArrayList<Game>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2
    private lateinit var circleIndicator3: CircleIndicator3
    private var canScrollToTop = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeBinding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding.apply {
            setViewPagerAndCircleIndicator(homeViewPager, homeCircleIndicator, view)
            setRecyclerView(homeRecyclerView, view)
        }

        setSearchView()
        setGames()
        loadStateVisibilityControl()

    }

    private fun setGames() {
        if (Helper.isNetworkAvailable(requireContext())) {
            videoGamesViewModel.getGamesApi(page = DEFAULT_VIEW_PAGER_PAGE,
                pageSize = DEFAULT_VIEW_PAGER_PAGE_SIZE)
                .observe(viewLifecycleOwner, { pageResult ->
                    games = pageResult.games as ArrayList<Game>
                    DataHolder.getInstance().gamesToRemove = games
                    viewPagerAdapter.setGames(games)
                    circleIndicator3.setViewPager(viewPager2)
                })

            videoGamesViewModel.games.observe(viewLifecycleOwner) { pagingDataGame ->
                gamesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingDataGame)
            }
        } else {
            fragmentHomeBinding.homeNotFound.text = getString(R.string.no_internet)
        }
    }

    private fun loadStateVisibilityControl() {
        gamesAdapter.addLoadStateListener { loadState ->
            fragmentHomeBinding.apply {
                homeNotFound.apply {
                    visibility =
                        if (gamesAdapter.itemCount < 1 && loadState.source.refresh is LoadState.NotLoading) {
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
                    canScrollToTop = true
                    if (!newText.isNullOrBlank()) {
                        canScrollToTop = true
                        Log.d("scroll !isNullOrBlank", "not here $canScrollToTop")
                    } else {
                        scrollToPositionTop()
                        Log.d("scroll isNullOrBlank", "here can $canScrollToTop")
                    }
                    searchBehaviour(newText)
                    return true
                }
            })
        }
    }

    private fun searchBehaviour(newText: String?) {
        if (!newText.isNullOrEmpty()) {
            if (newText.length >= DEFAULT_START_SEARCH_TEXT_LENGTH) {
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
            viewVisibility(viewPager2, false)
            viewVisibility(circleIndicator3, false)
        }
    }

    private fun scrollToPositionTop() {
        gamesAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && !canScrollToTop) {
                Log.d("scroll isNullOrBlank", "wtf can $canScrollToTop")
                fragmentHomeBinding.homeRecyclerView.scrollToPosition(0)
                canScrollToTop = false
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
            gamesAdapter =
                GamesAdapter { game ->
                    onClickItem(view, game)
                }
            adapter = gamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun onClickItem(view: View, game: Game) {
        val action = HomeFragmentDirections.actionNavigationHomeToGameFragment(game.id)
        Navigation.findNavController(view).navigate(action)
    }
}