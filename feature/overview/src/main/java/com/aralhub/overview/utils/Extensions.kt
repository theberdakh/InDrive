package com.aralhub.overview.utils

import android.content.Context
import android.location.LocationManager


fun isGPSEnabled(mContext: Context): Boolean {
    val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}