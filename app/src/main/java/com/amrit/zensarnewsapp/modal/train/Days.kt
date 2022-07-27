package com.amrit.zensarnewsapp.modal.train

import com.google.gson.annotations.SerializedName

data class Days(
    @SerializedName("Thu")
    val thu: String = "",
    @SerializedName("Tue")
    val tue: String = "",
    @SerializedName("Sat")
    val sat: String = "",
    @SerializedName("Wed")
    val wed: String = "",
    @SerializedName("Fri")
    val fri: String = "",
    @SerializedName("Mon")
    val mon: String = "",
    @SerializedName("Sun")
    val sun: String = ""
)