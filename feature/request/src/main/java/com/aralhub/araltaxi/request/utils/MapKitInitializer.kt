package com.aralhub.araltaxi.request.utils

import android.content.Context
import com.yandex.mapkit.MapKitFactory

/**
 * To init MapKit anywhere: https://github.com/yandex/mapkit-android-demo/issues/221#issuecomment-954451998*/
object MapKitInitializer {
    private var isInitialized = false

    fun init(apiKey: String, context: Context) {
        if (isInitialized){
            return
        }
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.setLocale("ru_RU")
        MapKitFactory.initialize(context)
        isInitialized = true
    }
}