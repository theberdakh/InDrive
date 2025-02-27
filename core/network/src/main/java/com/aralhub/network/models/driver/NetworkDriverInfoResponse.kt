package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverInfoResponse(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: Int,
    val color: NetworkMultiLanguageText,
    @SerializedName("vehicle_type")
    val vehicleType: NetworkMultiLanguageText,
    @SerializedName("plate_number")
    val plateNumber: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)

data class NetworkMultiLanguageText(
    val ru: String,
    val en: String,
    val kk: String
)
