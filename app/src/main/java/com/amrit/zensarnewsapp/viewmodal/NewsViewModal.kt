package com.amrit.zensarnewsapp.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.constants.StringConstants.API_ERROR_MESSAGE
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.modal.repository.NewsRepository
import com.amrit.zensarnewsapp.network.Response
import kotlinx.coroutines.launch

class NewsViewModal : ViewModel() {

    private var newsData = MutableLiveData<Response<List<Articles>>>()
    val newsHeadLines: LiveData<Response<List<Articles>>>
        get() = newsData

    fun getNewsHeadline(countryCode: String = NetworkAPIConstants.COUNTRY_USA) {
        newsData.postValue(Response.loading(null))
        viewModelScope.launch {
            val data = NewsRepository().fetchNewsData(countryCode)
            if (data?.isNotEmpty() == true)
                newsData.postValue(Response.success(data))
            else
                newsData.postValue(Response.error(null, API_ERROR_MESSAGE))
        }
    }
}