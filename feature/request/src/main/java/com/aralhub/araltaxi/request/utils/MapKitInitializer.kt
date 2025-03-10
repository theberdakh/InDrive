package com.aralhub.araltaxi.request.utils

import android.content.Context
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.i18n.I18nManager
import com.yandex.runtime.i18n.I18nManagerFactory

/**
 * To init MapKit anywhere: https://github.com/yandex/mapkit-android-demo/issues/221#issuecomment-954451998*/
object MapKitInitializer {
    private var isInitialized = false

    fun init(apiKey: String, context: Context) {
        if (isInitialized){
            return
        }
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.setLocale("uz_Uz")
        MapKitFactory.initialize(context)
        MapKitFactory.getInstance().onStart()
        isInitialized = true
    }
}