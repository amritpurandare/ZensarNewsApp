package com.amrit.zensarnewsapp.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.databinding.FragmentNewsDetailsBinding
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.viewmodal.NewsViewModal
import com.squareup.picasso.Picasso

class NewsDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflate = DataBindingUtil.inflate<FragmentNewsDetailsBinding>(
            inflater,
            R.layout.fragment_news_details,
            container,
            false
        )

        inflate.lifecycleOwner = requireActivity()
        val viewModel = ViewModelProvider(requireActivity())[NewsViewModal::class.java]
        inflate.viewModel = viewModel

        viewModel.articles.observe(requireActivity(), Observer { article ->
            article?.let {
                initViewWithData(article, inflate.root)
            }
        })

        return inflate.root
    }

    private fun initViewWithData(
        article: Articles,
        view: View
    ) {
        if (!TextUtils.isEmpty(article.urlToImage))
            Picasso.with(requireActivity()).load(article.urlToImage)
                .placeholder(android.R.drawable.presence_offline)
                .into(view.findViewById<ImageView>(R.id.articleImage))
    }
}