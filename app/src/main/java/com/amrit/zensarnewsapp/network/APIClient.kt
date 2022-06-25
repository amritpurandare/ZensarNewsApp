package com.amrit.zensarnewsapp.network

import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    /*
    * @param baseUrl - getting it from function params. WIll help to change base URL easily
    * */
    private fun getRetrofitClient(baseUrl: String): Retrofit {

        // This will help us to see the API logs in logcat
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        // The HTTP client used for requests
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getApiService(url: String = NetworkAPIConstants.BASE_URL): ApiService {
        return getRetrofitClient(url).create(ApiService::class.java)
    }
}