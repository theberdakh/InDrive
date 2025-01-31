package com.aralhub.indrive

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InDriveApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}