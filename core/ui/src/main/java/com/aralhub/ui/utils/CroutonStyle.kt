package com.aralhub.ui.utils

import com.aralhub.ui.R
import com.aralhub.ui.components.crouton.Configuration
import com.aralhub.ui.components.crouton.Style

object CroutonInDriveStyle {

    val errorStyle: Style =
        Style.Builder()
            .setBackgroundColor(R.color.color_error_crouton)
            .setTextAppearance(R.style.TextAppearance_InDrive_CroutonText)
            .setConfiguration(
                Configuration.Builder().setDuration(Configuration.DURATION_LONG).build()
            ).build()

    val successStyle: Style =
        Style.Builder().setBackgroundColor(R.color.color_success_crouton)
            .setTextAppearance(R.style.TextAppearance_InDrive_CroutonText)
            .setConfiguration(
                Configuration.Builder().setDuration(Configuration.DURATION_SHORT).build()
            ).build()
}