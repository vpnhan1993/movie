package com.example.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(private val listMovies: List<Movie>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoadMore = false

    inner class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var ivPoster: AppCompatImageView = itemView.findViewById(R.id.iv_poster)
        private var tvTitle: AppCompatTextView = itemView.findViewById(R.id.tv_title)

        fun bindData(data: Movie) {
            Picasso.get().load(data.poster).into(ivPoster)
            tvTitle.text = data.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (HolderEnum.getHolderType(viewType)) {
            HolderEnum.FOOTER -> {
                LoadMoreViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_load_more, parent, false)
                )
            }
            else -> {
                MovieViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) holder.bindData(data = listMovies[position])
    }

    override fun getItemCount(): Int {
        return listMovies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position) && isLoadMore) HolderEnum.FOOTER.ordinal else HolderEnum.ITEM.ordinal
    }

    fun isPositionFooter(position: Int): Boolean {
        return position == itemCount - 1 && isLoadMore
    }
}