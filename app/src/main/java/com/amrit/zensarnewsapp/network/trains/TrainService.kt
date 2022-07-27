package com.amrit.zensarnewsapp.network.trains

import com.amrit.zensarnewsapp.modal.train.TrainData
import com.amrit.zensarnewsapp.modal.train.Trains
import retrofit2.Response
import retrofit2.http.*

interface TrainService {

    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Key: 7c003fd841msh543306ded9e4b4fp1eb293jsn859f1cc4c14e",
        "X-RapidAPI-Host: trains.p.rapidapi.com"
    )
    @POST
    suspend fun getTrainStatus(@Body trainData: TrainData, @Url url: String): Response<List<Trains>>
}