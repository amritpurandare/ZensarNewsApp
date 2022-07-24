package com.amrit.zensarnewsapp.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amrit.zensarnewsapp.databinding.ListItemHeadlineBinding
import com.amrit.zensarnewsapp.modal.Articles
import com.squareup.picasso.Picasso

class NewsListAdapter(private val itemClick: OnNewsRowClick) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private var _binding: ListItemHeadlineBinding?= null
    private val binding get() = _binding!!

    var newsList = ArrayList<Articles>()

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        _binding =
            ListItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        binding.newsTitle.text = newsList[position].title
        if (!TextUtils.isEmpty(newsList[position].urlToImage))
            Picasso.with(holder.itemView.context)
                .load(newsList[position].urlToImage)
                .placeholder(android.R.drawable.presence_offline)
                .into(binding.newsImage)
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