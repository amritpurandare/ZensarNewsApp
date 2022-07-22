package com.amrit.zensarnewsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.viewmodal.NewsViewModal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsHeadlinesActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_headlines)
        viewModel = ViewModelProvider(this)[NewsViewModal::class.java]
        viewModel.showNewsDetailsFragment.observe(this, Observer { showArticleDetails ->
            if (showArticleDetails)
                showDetailsFragment()
            else
                showListFragment()
        })
    }

    override fun onBackPressed() {
        if (findViewById<View>(R.id.newsDetailsFragment).visibility == View.VISIBLE) {
            viewModel.showNewsDetailsFragment.postValue(false)
            return
        }
        super.onBackPressed()
    }

    private fun showListFragment() {
        findViewById<View>(R.id.newsDetailsFragment).visibility = View.GONE
        findViewById<View>(R.id.newsListFragment).visibility = View.VISIBLE
    }

    private fun showDetailsFragment() {
        findViewById<View>(R.id.newsListFragment).visibility = View.GONE
        findViewById<View>(R.id.newsDetailsFragment).visibility = View.VISIBLE
    }
}