package com.aralhub.araltaxi.core.common.error

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.aralhub.ui.components.crouton.Crouton
import com.aralhub.ui.components.crouton.Style
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val activity: Activity
) : ErrorHandler {
    private var isToastShowing = false
    override fun showToast(message: String, duration: Int) {
        Handler(Looper.getMainLooper()).post {
            val toast = Toast.makeText(context, message, duration)
            if (isToastShowing) {
                toast.cancel()
                toast.show()
            } else {
                toast.show()
            }

        }
    }

    override fun showCrouton(message: String, style: Style) {
        Crouton.makeText(activity, message, style).show()
    }
}