package com.aralhub.indrive.request.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionsHelper(
    private val activity: ComponentActivity,
    private val lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver {


    private lateinit var requestLauncher: ActivityResultLauncher<Array<String>>
    private var permissionCallback: (Map<String, Boolean>) -> Unit = {}

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        requestLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionCallback.invoke(it)
            }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return activity.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun arePermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all { isPermissionGranted(it) }
    }

    fun requestPermissions(
        permissions: Array<String>,
        onResult: (Map<String, Boolean>) -> Unit,
    ) {
        permissionCallback = onResult
        requestLauncher.launch(permissions)
    }

}