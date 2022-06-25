package com.amrit.zensarnewsapp.modal

import com.google.gson.annotations.SerializedName

data class NewsHeadlines(
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalResults") var totalResults: Int? = null,
    @SerializedName("articles") var articles: ArrayList<Articles> = arrayListOf()
)
