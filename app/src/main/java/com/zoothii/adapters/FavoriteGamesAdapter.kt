package com.zoothii.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zoothii.data.models.Game
import com.zoothii.videogames.databinding.ItemRecyclerViewBinding

class FavoriteGamesAdapter(
    @Nullable private val clickListener: ((Game) -> Unit)? = null,

    ) : RecyclerView.Adapter<FavoriteGamesAdapter.LikedGamesViewHolder>() {

    private var games: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedGamesViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LikedGamesViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: LikedGamesViewHolder, position: Int) {
        val currentGame: Game = games[position]
        holder.bind(currentGame)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener.invoke(currentGame) }
        }
        if (holder.itemView.visibility == View.VISIBLE) {
            holder.setMarquee()
        }
    }

    fun getGameAt(position: Int): Game {
        return games[position]
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun setGames(games: List<Game>) {
        this.games = games
        this.notifyDataSetChanged()
    }

    inner class LikedGamesViewHolder(
        private val itemRecyclerViewBinding: ItemRecyclerViewBinding,
    ) : RecyclerView.ViewHolder(
        itemRecyclerViewBinding.root
    ) {

        fun bind(currentGame: Game) {
            itemRecyclerViewBinding.apply {
                val releasedAt = "Released at ${currentGame.released}"
                val rating = "Rating ${currentGame.rating}"
                gameImageBackground.load(currentGame.backgroundImage)
                gameName.text = currentGame.name
                gameReleased.text = releasedAt
                gameRating.text = rating
            }
        }

        fun setMarquee() {
            itemRecyclerViewBinding.gameName.isSelected = true
        }
    }
}
