package com.aralhub.indrive.request.permissions

sealed class Permissions(vararg val permissions: String) {
    data object Location : Permissions(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    companion object {
        const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        const val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
    }
}