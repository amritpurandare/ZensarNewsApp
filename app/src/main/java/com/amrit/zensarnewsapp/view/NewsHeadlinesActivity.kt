package com.amrit.zensarnewsapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amrit.zensarnewsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsHeadlinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_headlines)
    }
}