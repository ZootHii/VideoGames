package com.zoothii.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zoothii.data.models.Game
import com.zoothii.videogames.databinding.ItemViewPagerBinding


class ViewPagerAdapter(
    @Nullable private val clickListener: ((Game) -> Unit)? = null,
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private var games: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val itemViewPagerBinding = ItemViewPagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPagerViewHolder(itemViewPagerBinding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentGame: Game = games[position]
        holder.bind(currentGame)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener.invoke(currentGame) }

        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun setGames(games: List<Game>) {
        this.games = games
        this.notifyDataSetChanged()
    }

    inner class ViewPagerViewHolder(
        private val itemViewPagerBinding: ItemViewPagerBinding,

        ) : RecyclerView.ViewHolder(
        itemViewPagerBinding.root
    ) {

        fun bind(currentGame: Game) {
            itemViewPagerBinding.imageGameBackground.load(currentGame.backgroundImage)
        }
    }
}
