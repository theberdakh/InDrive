package com.aralhub.indrive

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.WindowManager

class WindowModeLogger {
    companion object {
        private const val TAG = "WindowModeLogger"

        fun logSoftInputMode(activity: Activity) {
            val softInputMode = activity.window.attributes.softInputMode
            
            val modes = mutableListOf<String>()
            
            // Check for SOFT_INPUT_ADJUST flags
            when (softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST) {
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING -> 
                    modes.add("SOFT_INPUT_ADJUST_NOTHING")
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN -> 
                    modes.add("SOFT_INPUT_ADJUST_PAN")
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE -> 
                    modes.add("SOFT_INPUT_ADJUST_RESIZE")
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED -> 
                    modes.add("SOFT_INPUT_ADJUST_UNSPECIFIED")
            }
            
            // Check for SOFT_INPUT_STATE flags
            when (softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE) {
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN -> 
                    modes.add("SOFT_INPUT_STATE_ALWAYS_HIDDEN")
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE -> 
                    modes.add("SOFT_INPUT_STATE_ALWAYS_VISIBLE")
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN -> 
                    modes.add("SOFT_INPUT_STATE_HIDDEN")
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE -> 
                    modes.add("SOFT_INPUT_STATE_VISIBLE")
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED -> 
                    modes.add("SOFT_INPUT_STATE_UNCHANGED")
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED -> 
                    modes.add("SOFT_INPUT_STATE_UNSPECIFIED")
            }

            // Get the manifest value
            val activityInfo = activity.packageManager
                .getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
            
            Log.d(TAG, "Activity: ${activity.javaClass.simpleName}")
            Log.d(TAG, "Manifest softInputMode: ${
                when (activityInfo.softInputMode) {
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING -> "adjustNothing"
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN -> "adjustPan"
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE -> "adjustResize"
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED -> "adjustUnspecified"
                    else -> "unknown (${activityInfo.softInputMode})"
                }
            }")
            Log.d(TAG, "Current window modes: ${modes.joinToString(", ")}")
        }
    }
}