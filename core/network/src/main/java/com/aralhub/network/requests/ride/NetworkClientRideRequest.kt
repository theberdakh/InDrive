package com.aralhub.network.requests.ride

import com.aralhub.network.models.location.NetworkLocations
import com.aralhub.network.models.price.NetworkRecommendedPrice
import com.google.gson.annotations.SerializedName

data class NetworkClientRideRequest(
    @SerializedName("passenger_id")
    val passengerId: Int,
    @SerializedName("base_amount")
    val baseAmount: Number,
    @SerializedName("recommended_amount")
    val recommendedAmount: NetworkRecommendedPrice,
    val locations: NetworkLocations,
    val comment: String,
    @SerializedName("auto_take")
    val autoTake: Boolean,
    @SerializedName("payment_id")
    val paymentId: Int,
    @SerializedName("option_ids")
    val optionIds: List<Int>,
    @SerializedName("cancel_cause_id")
    val cancelCauseId: Int? = null
)