package com.aralhub.network.models.driver

import com.aralhub.network.models.NetworkMultiLanguageText
import com.google.gson.annotations.SerializedName

data class NetworkDriverOffer(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: Float,
    @SerializedName("photo_url")
    val photoUrl: String,
    val color: NetworkMultiLanguageText,
    @SerializedName("vehicle_type")
    val vehicleType: NetworkMultiLanguageText,
    @SerializedName("plate_number")
    val plateNumber: String,
)
