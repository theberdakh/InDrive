package com.aralhub.network.models.driver

import com.aralhub.network.models.NetworkMultiLanguageText
import com.aralhub.network.models.option.NetworkOption
import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.google.gson.annotations.SerializedName

data class NetworkDriverActive(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: Number,
    @SerializedName("photo_url")
    val photoUrl: String,
    @SerializedName("color")
    val vehicleColor: NetworkMultiLanguageText,
    @SerializedName("vehicle_type")
    val vehicleType: NetworkMultiLanguageText,
    @SerializedName("plate_number")
    val plateNumber: String,
    @SerializedName("phone_number")
    val phoneNumber: String?,
)