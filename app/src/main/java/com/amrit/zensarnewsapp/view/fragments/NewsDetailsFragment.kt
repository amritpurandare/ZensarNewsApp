package com.amrit.zensarnewsapp.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.databinding.FragmentNewsDetailsBinding
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.viewmodal.NewsViewModal
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class NewsDetailsFragment : Fragment() {

    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate<FragmentNewsDetailsBinding>(
            inflater,
            R.layout.fragment_news_details,
            container,
            false
        )

        binding.lifecycleOwner = requireActivity()
        val viewModel = ViewModelProvider(requireActivity())[NewsViewModal::class.java]
        binding.viewModel = viewModel

        viewModel.articles.observe(requireActivity(), Observer { article ->
            article?.let {
                initViewWithData(article, binding.root)
            }
        })
        logNewsDetailsScreenViewEvent()
        return binding.root
    }

    private fun initViewWithData(
        article: Articles,
        view: View
    ) {
        if (!TextUtils.isEmpty(article.urlToImage))
            Picasso.with(activity).load(article.urlToImage)
                .placeholder(android.R.drawable.presence_offline)
                .into(binding.articleImage)
    }

    private fun logNewsDetailsScreenViewEvent() {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.fb_details_screen_name))
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "NewsDetailsFragment")
        }
    }
}