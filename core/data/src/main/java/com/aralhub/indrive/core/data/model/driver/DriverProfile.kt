package com.aralhub.indrive.core.data.model.driver

data class DriverProfile(
    val driverId: Int,
    val fullName: String,
    val rating: Int,
    val color: String,
    val vehicleType: String,
    val plateNumber: String,
    val phoneNumber: String
)