package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverInfoResponse(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: Int,
    val color: String,
    @SerializedName("vehicle_type")
    val vehicleType: String,
    @SerializedName("plate_number")
    val plateNumber: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)
