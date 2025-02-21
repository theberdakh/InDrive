package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverInfoResponse(
    val address: String,
    @SerializedName("license_number")
    val licenseNumber: String,
    @SerializedName("date_of_issue")
    val dateOfIssue: String,
    @SerializedName("date_of_expiry")
    val dateOfExpiry: String,
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("name_on_card")
    val nameOnCard: String,
    val id: Int,
    @SerializedName("driver_id")
    val driverId: Int,
    @SerializedName("front_photo_url")
    val frontPhotoUrl: String?,
    @SerializedName("back_photo_url")
    val backPhotoUrl: String?
)
