package com.amrit.zensarnewsapp.modal.repository

import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.constants.StringConstants
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.network.ApiService
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchNewsData(countryCode: String? = NetworkAPIConstants.COUNTRY_USA): ArrayList<Articles>? {
        if (countryCode != null) {
            val headlines =
                apiService.getHeadlines(countryCode, NetworkAPIConstants.API_KEY)
            if (headlines?.isSuccessful == true) {
                headlines.body()?.run {
                    if (status == StringConstants.API_STATUS_OK) {
                        return articles
                    }
                }
            }
        }
        return null
    }
}