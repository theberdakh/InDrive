package com.aralhub.network.models.cancel

import com.google.gson.annotations.SerializedName

data class NetworkCancelCause(
    val name: String,
    val type: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    val id: Int
)

data class NetworkDriverCancelCause(
    val id: Int,
    val name: String
)
