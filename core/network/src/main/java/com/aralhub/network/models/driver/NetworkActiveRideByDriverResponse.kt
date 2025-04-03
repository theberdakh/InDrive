package com.aralhub.network.models.driver

import com.aralhub.network.models.offer.Locations
import com.aralhub.network.models.websocketclient.ClientRideResponsePassenger
import com.aralhub.network.models.websocketclient.ClientRideResponsePaymentMethod
import com.google.gson.annotations.SerializedName

data class NetworkActiveRideByDriverResponse(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    @SerializedName("wait_amount")
    val waitAmount: Double,
    val distance: Float,
    val locations: Locations,
    @SerializedName("is_active")
    val isActive: Boolean,
    val passenger: ClientRideResponsePassenger,
    @SerializedName("payment_method")
    val paymentMethod: ClientRideResponsePaymentMethod
)
