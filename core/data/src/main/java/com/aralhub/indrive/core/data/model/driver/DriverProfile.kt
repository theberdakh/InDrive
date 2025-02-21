package com.aralhub.indrive.core.data.model.driver

data class DriverProfile(
    val id: Int,
    val driverId: Int,
    val address: String,
    val licenseNumber: String,
    val dateOfIssue: String,
    val dateOfExpiry: String,
    val cardNumber: String,
    val cardHolder: String,
    val frontPhotoUrl: String,
    val backPhotoUrl: String,
)