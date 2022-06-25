package com.amrit.zensarnewsapp.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.modal.Articles
import com.squareup.picasso.Picasso

class NewsListAdapter(private val itemClick: OnNewsRowClick) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    var newsList = ArrayList<Articles>()

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.newsTitle)
        val icon: ImageView = itemView.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_headline, parent, false)
        return NewsViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.text = newsList[position].title
        if (!TextUtils.isEmpty(newsList[position].urlToImage))
            Picasso.with(holder.itemView.context)
                .load(newsList[position].urlToImage)
                .placeholder(android.R.drawable.presence_offline)
                .into(holder.icon)
        holder.itemView.setOnClickListener {
            itemClick.onNewsArticleClick(newsList[position])
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setAdapterData(headlines: List<Articles>) {
        this.newsList = headlines as ArrayList<Articles>
        notifyDataSetChanged()
    }
}

interface OnNewsRowClick {
    fun onNewsArticleClick(article: Articles)
}