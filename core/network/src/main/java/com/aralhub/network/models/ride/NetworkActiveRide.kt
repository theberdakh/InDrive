package com.aralhub.network.models.ride

import com.google.gson.annotations.SerializedName

data class NetworkActiveRideResponse(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    @SerializedName("wait_amount")
    val waitAmount: Int,
    val distance: Int,
    val locations: List<Int>,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    val driver: NetworkActiveRideDriver,
    @SerializedName("payment_method")
    val paymentMethod: NetworkActiveRidePaymentMethod,
    val options: List<NetworkActiveRideOptions>
)

data class NetworkActiveRideDriver(
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("full_name")
    val fullName: String,
    val rating: String,
    @SerializedName("color")
    val vehicleColor: NetworkActiveRideVehicleColor,
    @SerializedName("vehicle_type")
    val vehicleType: NetworkActiveRideVehicleType,
    @SerializedName("plate_number")
    val vehicleNumber: String
)

data class NetworkActiveRideVehicleColor(
    val ru: String,
    val en: String,
    val kk: String
)

data class NetworkActiveRideVehicleType(
    val ru: String,
    val en: String,
    val kk: String
)

data class NetworkActiveRidePaymentMethod(
    val id: Int,
    val name: String,
    @SerializedName("is_active")
    val isActive: Boolean
)

data class NetworkActiveRideOptions(
    val id: Int,
    val name: String
)