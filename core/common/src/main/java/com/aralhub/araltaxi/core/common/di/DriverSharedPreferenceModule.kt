package com.aralhub.araltaxi.core.common.di

import android.content.Context
import com.aralhub.araltaxi.core.common.sharedpreference.DriverSharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DriverSharedPreferenceModule {
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): DriverSharedPreference {
        val preference = context.getSharedPreferences("DriverSharedPreference", Context.MODE_PRIVATE)
        return DriverSharedPreference(preference)
    }
}