package com.aralhub.araltaxi.core.common.error

import android.view.View
import android.widget.Toast
import com.aralhub.ui.components.crouton.Style
import com.aralhub.ui.utils.CroutonInDriveStyle
import com.google.android.material.snackbar.Snackbar

interface ErrorHandler {
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)
    fun showCrouton(message: String, style: Style = CroutonInDriveStyle.errorStyle)
}