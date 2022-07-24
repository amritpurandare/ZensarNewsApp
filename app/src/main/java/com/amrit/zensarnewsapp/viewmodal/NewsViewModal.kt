package com.amrit.zensarnewsapp.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.constants.StringConstants.API_ERROR_MESSAGE
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.modal.repository.NewsRepository
import com.amrit.zensarnewsapp.network.Response
import com.amrit.zensarnewsapp.utils.ConnectivityManager
import com.amrit.zensarnewsapp.utils.SharedPrefHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModal @Inject constructor() : ViewModel() {

    @Inject
    lateinit var newsRepository: NewsRepository

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    var articles = MutableLiveData<Articles>()
    private var newsData = MutableLiveData<Response<List<Articles>>>()
    val newsHeadLines: LiveData<Response<List<Articles>>>
        get() = newsData

    fun getNewsHeadline() {
        if (connectivityManager.isNetworkAvailable()) {
            callNewsApi()
        } else
            newsData.postValue(Response.error(null, API_ERROR_MESSAGE))
    }

    fun getUserCountry(): String {
        return sharedPrefHelper.getUserCountry()
    }

    private fun callNewsApi() {
        newsData.postValue(Response.loading(null))
        viewModelScope.launch {
            val data = newsRepository.fetchNewsData(getUserCountry())
            if (data?.isNotEmpty() == true)
                newsData.postValue(Response.success(data))
            else
                newsData.postValue(Response.error(null, API_ERROR_MESSAGE))
        }
    }

    fun handleMenuItemClick(itemId: Int) {
        val country = getUserCountry()
        when (itemId) {
            R.id.usa -> {
                if (country != NetworkAPIConstants.COUNTRY_USA) {
                    changeCountryAndFetchData(NetworkAPIConstants.COUNTRY_USA)
                }
            }
            R.id.canada -> {
                if (country != NetworkAPIConstants.COUNTRY_CANADA) {
                    changeCountryAndFetchData(NetworkAPIConstants.COUNTRY_CANADA)
                }
            }
        }
    }

    private fun changeCountryAndFetchData(country: String) {
        sharedPrefHelper.saveUserChoiceCountry(country)
        getNewsHeadline()
    }
}