package com.amrit.zensarnewsapp.utils

import android.content.Context
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants.COUNTRY_USA
import com.amrit.zensarnewsapp.constants.StringConstants.SHARED_PREF_COUNTRY
import com.amrit.zensarnewsapp.constants.StringConstants.SHARED_PREF_NAME

class SharedPrefHelper(private val context: Context) {

    fun saveUserChoiceCountry(country: String) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(SHARED_PREF_COUNTRY, country)
            .apply()
    }

    fun getUserCountry(): String {
        val savedCountry = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .getString(SHARED_PREF_COUNTRY, COUNTRY_USA)
        return savedCountry ?: COUNTRY_USA
    }
}