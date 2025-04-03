package com.aralhub.network.models.ride

import com.google.gson.annotations.SerializedName

data class NetworkRideCompleted(
    @SerializedName("ride_id")
    val rideId: Int,
    @SerializedName("driver_id")
    val driverId: Int,
    val amount: Double,
    @SerializedName("wait_amount")
    val waitAmount: Double,
    @SerializedName("base_amount")
    val baseAmount: Double,
    @SerializedName("cashback_amount")
    val cashbackAmount: Double,
    val duration: Double,
    val distance: Double,
    @SerializedName("payment_method")
    val paymentMethod: String,
    val status: String
)
