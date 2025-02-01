package com.aralhub.indrive.request.model

import androidx.annotation.DrawableRes

data class LocationItem(
    val id: Int,
    val title: String,
    @DrawableRes val icon: Int,
    val subtitle: String
)
