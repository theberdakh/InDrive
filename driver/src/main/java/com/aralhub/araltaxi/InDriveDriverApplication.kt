package com.aralhub.araltaxi

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InDriveDriverApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("f1c206ee-1f73-468c-8ba8-ec3ef7a7f69a")
    }
}