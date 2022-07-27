package com.amrit.zensarnewsapp

import com.amrit.zensarnewsapp.network.trains.TrainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TrainNetworkModule {

    @Singleton
    @Provides
    @Named("Train")
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://trains.p.rapidapi.com/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesTrainService(@Named("Train") retrofit: Retrofit): TrainService {
        return retrofit.create(TrainService::class.java)
    }

}