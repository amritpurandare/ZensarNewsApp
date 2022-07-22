package com.amrit.zensarnewsapp.utils

import android.content.SharedPreferences
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants.COUNTRY_USA
import com.amrit.zensarnewsapp.constants.StringConstants.SHARED_PREF_COUNTRY
import javax.inject.Inject

class SharedPrefHelper @Inject constructor() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    fun saveUserChoiceCountry(country: String) {
        sharedPreferences.edit().putString(SHARED_PREF_COUNTRY, country)
            .apply()
    }

    fun getUserCountry(): String {
        val savedCountry = sharedPreferences.getString(SHARED_PREF_COUNTRY, COUNTRY_USA)
        return savedCountry ?: COUNTRY_USA
    }
}