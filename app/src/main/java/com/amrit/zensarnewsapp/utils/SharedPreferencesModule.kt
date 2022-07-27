package com.amrit.zensarnewsapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.amrit.zensarnewsapp.constants.StringConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferencesModule {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(StringConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}