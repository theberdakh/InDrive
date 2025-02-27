package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class DriverInfoWithVehicleResponse(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: Number,
    val color: NetworkMultiLanguageText,
    @SerializedName("vehicle_type")
    val vehicleType: NetworkMultiLanguageText,
    @SerializedName("plate_number")
    val plateNumber: String

)