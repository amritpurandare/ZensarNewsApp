package com.amrit.zensarnewsapp.modal.train

import com.google.gson.annotations.SerializedName

data class Trains(
    @SerializedName("train_from")
    val trainFrom: String = "",
    @SerializedName("train_num")
    val trainNum: Int = 0,
    @SerializedName("data")
    val data: Data,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("train_to")
    val trainTo: String = ""
)