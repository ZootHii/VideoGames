package com.zoothii.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zoothii.models.Game
import com.zoothii.videogames.databinding.ItemRecyclerViewBinding

class GamesAdapter(
    @Nullable private val clickListener: ((Game) -> Unit)? = null,
) : PagingDataAdapter<Game, GamesAdapter.RecyclerViewViewHolder>(GAME_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {

        //Log.d("log position", getItem(position).toString())

        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bindItems(currentItem)
            if (clickListener != null) {
                holder.itemView.setOnClickListener { clickListener.invoke(currentItem) }
            }
        }

    }

    inner class RecyclerViewViewHolder(
        private val itemRecyclerViewBinding: ItemRecyclerViewBinding,
    ) : RecyclerView.ViewHolder(
        itemRecyclerViewBinding.root
    ) {

        fun bindItems(currentGame: Game) {
            itemRecyclerViewBinding.apply {
                val releasedAt = "Released at ${currentGame.released}"
                val rating = "Rating ${currentGame.rating}"
                gameImageBackground.load(currentGame.backgroundImage)
                gameName.text = currentGame.name
                gameReleased.text = releasedAt
                gameRating.text = rating
            }
        }
    }

    companion object {
        private val GAME_COMPARATOR = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Game, newItem: Game) =
                oldItem.backgroundImage == newItem.backgroundImage
        }
    }
}
