package com.aralhub.araltaxi.core.common.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionHelper {
    fun arePermissionsGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.all { permission ->
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getDeniedPermissions(context: Context, permissions: List<String>): List<String> {
        return permissions.filter { permission ->
            ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }

    fun isAnyPermissionGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.any { permission ->
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}