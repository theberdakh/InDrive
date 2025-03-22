package com.aralhub.network.models.ride

import com.aralhub.network.models.distance.NetworkDistance
import com.aralhub.network.models.driver.NetworkDriverSearch
import com.aralhub.network.models.location.NetworkLocations
import com.aralhub.network.models.option.NetworkOptions
import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.aralhub.network.models.price.NetworkRecommendedPrice
import com.google.gson.annotations.SerializedName

data class NetworkRideSearch(
    val uuid: String,
    val passenger: NetworkDriverSearch,
    @SerializedName("base_amount")
    val baseAmount: Number,
    @SerializedName("updated_amount")
    val updatedAmount: Number?,
    @SerializedName("recommended_amount")
    val recommendedAmount: NetworkRecommendedPrice,
    val locations: NetworkLocations,
    val comment: String,
    @SerializedName("payment_method")
    val paymentMethod: NetworkPaymentMethod,
    val options: NetworkOptions,
    @SerializedName("auto_take")
    val autoTake: Boolean,
    val distance: NetworkDistance,
    @SerializedName("cancel_cause_id")
    val cancelCauseId: Int,
)