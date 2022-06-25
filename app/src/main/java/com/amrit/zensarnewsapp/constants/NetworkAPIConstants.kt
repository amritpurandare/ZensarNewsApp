package com.amrit.zensarnewsapp.constants

object NetworkAPIConstants {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val NEWS_END_POINT = "top-headlines"

    // API KEY used to get top-headlines. Ideally should be saved into secured prefs or into keychain
    const val API_KEY = "793692c8957049078b42b65bf9b93021"

    // required to send in get request if user selects "USA" country
    const val COUNTRY_USA = "us"

    // required to send in get request if user selects "CANADA" country
    const val COUNTRY_CANADA = "ca"
}