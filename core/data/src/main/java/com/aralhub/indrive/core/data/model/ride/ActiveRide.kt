package com.aralhub.indrive.core.data.model.ride


data class ActiveRide(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    val waitAmount: Int,
    val distance: Int,
    val locations: List<Int>,
    val isActive: Boolean,
    val createdAt: String,
    val driver: ActiveRideDriver,
    val paymentMethod: ActiveRidePaymentMethod,
    val options: List<ActiveRideOptions>
)

data class ActiveRideDriver(
    val driverId: Int,
    val fullName: String,
    val rating: String,
    val vehicleColor: ActiveRideVehicleColor,
    val vehicleType: ActiveRideVehicleType,
    val vehicleNumber: String
)

data class ActiveRideVehicleColor(
    val ru: String,
    val en: String,
    val kk: String
)

data class ActiveRideVehicleType(
    val ru: String,
    val en: String,
    val kk: String
)

data class ActiveRidePaymentMethod(
    val id: Int,
    val name: String,
    val isActive: Boolean
)

data class ActiveRideOptions(
    val id: Int,
    val name: String
)