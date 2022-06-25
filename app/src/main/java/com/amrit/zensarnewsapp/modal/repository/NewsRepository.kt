package com.amrit.zensarnewsapp.modal.repository

import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.constants.StringConstants
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.network.APIClient

class NewsRepository {
    suspend fun fetchNewsData(countryCode: String = NetworkAPIConstants.COUNTRY_USA): ArrayList<Articles>? {
        val apiService = APIClient.getApiService()
        val headlines =
            apiService.getHeadlines(countryCode, NetworkAPIConstants.API_KEY)
        if (headlines.isSuccessful) {
            headlines.body()?.run {
                if (status == StringConstants.API_STATUS_OK) {
                    return articles
                }
            }
        }
        return null
    }
}