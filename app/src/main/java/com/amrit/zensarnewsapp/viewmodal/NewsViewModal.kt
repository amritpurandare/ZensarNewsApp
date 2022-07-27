package com.amrit.zensarnewsapp.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.modal.data.Article
import com.amrit.zensarnewsapp.modal.data.NewsHeadlines
import com.amrit.zensarnewsapp.modal.repository.NewsRepository
import com.amrit.zensarnewsapp.network.NewsApiResponse
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

    var article = MutableLiveData<Article>()

    val newsApiResponse: LiveData<NewsApiResponse<NewsHeadlines>>
        get() = newsRepository.newsApiResponse

    fun getUserCountry(): String {
        return sharedPrefHelper.getUserCountry()
    }

    fun callNewsApi() {
        viewModelScope.launch {
            newsRepository.fetchNewsData(getUserCountry())
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
        callNewsApi()
    }
}