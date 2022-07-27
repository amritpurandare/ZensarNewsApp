package com.amrit.zensarnewsapp.modal.train

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("arriveTime")
    val arriveTime: String = "",
    @SerializedName("from_id")
    val fromId: String = "",
    @SerializedName("departTime")
    val departTime: String = "",
    @SerializedName("classes")
    val classes: List<String>?,
    @SerializedName("days")
    val days: Days,
    @SerializedName("to_id")
    val toId: String = "",
    @SerializedName("id")
    val id: String = ""
)