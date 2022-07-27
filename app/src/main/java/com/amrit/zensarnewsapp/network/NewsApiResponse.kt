package com.amrit.zensarnewsapp.network

sealed class NewsApiResponse<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T> : NewsApiResponse<T>()
    class Success<T>(data: T? = null) : NewsApiResponse<T>(data = data)
    class Error<T>(errorMessage: String?, data: T? = null) :
        NewsApiResponse<T>(errorMessage = errorMessage, data = data)
}