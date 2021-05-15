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

/*class RecyclerViewAdapter(
    @NonNull private val context: Context,
    @NonNull private val fragmentHomeBinding: FragmentHomeBinding,
    @Nullable private val clickListener: ((Result) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>(), Filterable {


    private var gamesResult: List<Result> = ArrayList()
    private var gamesResultFull: List<Result> = ArrayList()
    private var gamesResultFilter: List<Result> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return RecyclerViewViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        val currentResult: Result = gamesResult[position]
        holder.bindItems(currentResult)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener.invoke(currentResult) }
        }
    }


    override fun getItemCount(): Int {
        return gamesResult.size
    }

    fun setGames(gamesResult: List<Result>, gamesResultFilter: List<Result>) {
        this.gamesResult = gamesResult
        this.gamesResultFull = gamesResult
        this.gamesResultFilter = gamesResultFilter
        this.notifyDataSetChanged()
    }

    inner class RecyclerViewViewHolder(
        private val itemRecyclerViewBinding: ItemRecyclerViewBinding,
    ) : RecyclerView.ViewHolder(
        itemRecyclerViewBinding.root
    ) {

        fun bindItems(currentResult: Result) {
            itemRecyclerViewBinding.apply {
                gameImageBackground.load(currentResult.backgroundImage)
                gameName.text = currentResult.name
                gameReleased.text = "Released at ${currentResult.released}"
                gameRating.text = "Rating ${currentResult.rating}"
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()
                if (constraint == null || constraint.length < 3) {
                    filterResults.count = gamesResultFilter.size
                    filterResults.values = gamesResultFilter
                } else {
                    val filterPattern: String =
                        constraint.toString().toLowerCase(Locale.ROOT).trim()
                    val filteredGames = ArrayList<Result>()
                    gamesResultFilter.forEach { result ->
                        if (result.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredGames.add(result)
                        }
                    }

                    Log.d("response game res1", filteredGames.toString())
                    Log.d("response game res3", gamesResultFilter.toString())
                    filterResults.count = filteredGames.size
                    filterResults.values = filteredGames
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredGames = results?.values as ArrayList<Result>

                Log.d("response filteredgame", filteredGames.toString())
                when {
                    filteredGames.isEmpty() -> {
                        fragmentHomeBinding.homeNotFound.visibility = View.VISIBLE
                    }
                    else -> {
                        fragmentHomeBinding.homeNotFound.visibility = View.GONE
                    }
                }
                if (gamesResultFilter == filteredGames) {
                    gamesResult = gamesResultFull
                } else {
                    gamesResult = filteredGames
                    Log.d("response game res2", gamesResult.toString())
                }



                notifyDataSetChanged()
            }
        }
    }
}*/


/*class RecyclerViewAdapter(
    @NonNull private val context: Context,
    @NonNull private val fragmentHomeBinding: FragmentHomeBinding,
    @Nullable private val clickListener: ((Result) -> Unit)? = null
) : PagingDataAdapter<Result, RecyclerViewAdapter.RecyclerViewViewHolder>(DiffUtilCallBack()) {

    inner class RecyclerViewViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(
        view
    ) {
        val image = view.findViewById<ImageView>(R.id.game_image_background)
        fun bind(data: Result) {
            image.load(data.backgroundImage)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.name == newItem.name && oldItem.released == newItem.released
        }

    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return RecyclerViewViewHolder(inflater)
    }

}*/


class RecyclerViewAdapter(
    @Nullable private val clickListener: ((Game) -> Unit)? = null,
) : PagingDataAdapter<Game, RecyclerViewAdapter.RecyclerViewViewHolder>(GAME_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

        val itemRecyclerViewBinding = ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerViewViewHolder(itemRecyclerViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {

        Log.d("log position", getItem(position).toString())

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

            override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
        }
    }
}
