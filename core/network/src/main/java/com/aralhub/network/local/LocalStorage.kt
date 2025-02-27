package com.aralhub.network.local

import android.content.SharedPreferences

class LocalStorage(preference: SharedPreferences) {

    var access by StringPreference(preference)

    var refresh by StringPreference(preference)

    var phoneNumber by StringPreference(preference)

    var isLogin by BooleanPreference(preference, false)

    fun clear() {
        access = ""
        refresh = ""
        phoneNumber = ""
        isLogin = false
    }

}