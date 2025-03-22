package com.aralhub.araltaxi.core.common.sharedpreference

import android.content.SharedPreferences

class DriverSharedPreference(preference: SharedPreferences) {

    var distance by IntPreference(preference, 3000)

}