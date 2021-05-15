/*
package com.zoothii.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zoothii.models.GameDetails
import com.zoothii.videogames.databinding.ItemRecyclerViewBinding

class LikedGamesAdapter(
    @Nullable private val clickListener: ((GameDetails) -> Unit)? = null,
) : RecyclerView.Adapter<LikedGamesAdapter.LikedGamesViewHolder>() {

    private var gameDetailsList: List<GameDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedGamesViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LikedGamesViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: LikedGamesViewHolder, position: Int) {
        val currentGameDetails: GameDetails = gameDetailsList[position]
        holder.bind(currentGameDetails)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener.invoke(currentGameDetails) }
        }
    }

    override fun getItemCount(): Int {
        return gameDetailsList.size
    }

    fun setGames(gameDetailsList: List<GameDetails>) {
        this.gameDetailsList = gameDetailsList
        this.notifyDataSetChanged()
    }

    inner class LikedGamesViewHolder(
        private val itemRecyclerViewBinding: ItemRecyclerViewBinding,
    ) : RecyclerView.ViewHolder(
        itemRecyclerViewBinding.root
    ) {

        fun bind(currentGameDetails: GameDetails) {
            itemRecyclerViewBinding.apply {
                val releasedAt = "Released at ${currentGameDetails.released}"
                val rating = "Rating ${currentGameDetails.rating}"
                gameImageBackground.load(currentGameDetails.backgroundImage)
                gameName.text = currentGameDetails.name
                gameReleased.text = releasedAt
                gameRating.text = rating
            }
        }
    }
}*/


// TODO TEST


package com.zoothii.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zoothii.models.Game
import com.zoothii.videogames.databinding.ItemRecyclerViewBinding

class LikedGamesAdapter(
    @Nullable private val clickListener: ((Game) -> Unit)? = null,
) : RecyclerView.Adapter<LikedGamesAdapter.LikedGamesViewHolder>() {

    private var gameDetailsList: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedGamesViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LikedGamesViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: LikedGamesViewHolder, position: Int) {
        val currentGameDetails: Game = gameDetailsList[position]
        holder.bind(currentGameDetails)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener.invoke(currentGameDetails) }
        }
    }

    override fun getItemCount(): Int {
        return gameDetailsList.size
    }

    fun setGames(gameDetailsList: List<Game>) {
        this.gameDetailsList = gameDetailsList
        this.notifyDataSetChanged()
    }

    inner class LikedGamesViewHolder(
        private val itemRecyclerViewBinding: ItemRecyclerViewBinding,
    ) : RecyclerView.ViewHolder(
        itemRecyclerViewBinding.root
    ) {

        fun bind(currentGameDetails: Game) {
            itemRecyclerViewBinding.apply {
                val releasedAt = "Released at ${currentGameDetails.released}"
                val rating = "Rating ${currentGameDetails.rating}"
                gameImageBackground.load(currentGameDetails.backgroundImage)
                gameName.text = currentGameDetails.name
                gameReleased.text = releasedAt
                gameRating.text = rating
            }
        }
    }
}
