package com.aralhub.araltaxi.core.common.di

import android.app.Activity
import android.content.Context
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.core.common.error.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
internal object CommonModule {

    @Provides
    fun provideErrorHandler(
        @ApplicationContext context: Context,
        activity: Activity
    ): ErrorHandler = ErrorHandlerImpl(context, activity)
}