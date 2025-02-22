package com.aralhub.indrive.core.data.model.driver

data class DriverProfileWithVehicle(
    val driverId: Int,
    val fullName: String,
    val rating: Number,
    val color: String,
    val vehicleType: String,
    val plateNumber: String
)
