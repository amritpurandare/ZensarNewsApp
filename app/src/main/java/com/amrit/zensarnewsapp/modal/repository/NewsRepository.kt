package com.amrit.zensarnewsapp.modal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.constants.StringConstants.API_ERROR_MESSAGE
import com.amrit.zensarnewsapp.modal.data.NewsHeadlines
import com.amrit.zensarnewsapp.network.ApiService
import com.amrit.zensarnewsapp.network.NewsApiResponse
import com.amrit.zensarnewsapp.utils.ConnectivityManager
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService) {
    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private val _newsApiResponse = MutableLiveData<NewsApiResponse<NewsHeadlines>>()
    val newsApiResponse: LiveData<NewsApiResponse<NewsHeadlines>> get() = _newsApiResponse

    suspend fun fetchNewsData(countryCode: String = NetworkAPIConstants.COUNTRY_USA) {
        if (connectivityManager.isNetworkAvailable()) {
            _newsApiResponse.postValue(NewsApiResponse.Loading())
            val headlines =
                apiService.getHeadlines(countryCode, NetworkAPIConstants.API_KEY)
            processApiResponse(headlines)
        } else {
            _newsApiResponse.postValue(NewsApiResponse.Error("No Internet connection."))
        }
    }

    private fun processApiResponse(headlines: Response<NewsHeadlines>?) {
        if (headlines?.isSuccessful == true && headlines.body() != null) {
            _newsApiResponse.postValue(NewsApiResponse.Success(headlines.body()))
        } else if (headlines?.errorBody() != null) {
            val errorObj = JSONObject(headlines.errorBody()!!.charStream().readText())
            _newsApiResponse.postValue(NewsApiResponse.Error(errorObj.getString("message")))
        } else {
            _newsApiResponse.postValue(NewsApiResponse.Error(API_ERROR_MESSAGE))
        }
    }
}