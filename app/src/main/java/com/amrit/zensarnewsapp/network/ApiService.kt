package com.amrit.zensarnewsapp.network

import com.amrit.zensarnewsapp.constants.NetworkAPIConstants.KEY_API_KEY
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants.KEY_COUNTRY
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants.NEWS_END_POINT
import com.amrit.zensarnewsapp.modal.NewsHeadlines
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(NEWS_END_POINT)
    suspend fun getHeadlines(
        @Query(KEY_COUNTRY) country: String,
        @Query(KEY_API_KEY) apiKey: String
    ): Response<NewsHeadlines>
}