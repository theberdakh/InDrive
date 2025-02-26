package com.aralhub.network.models.ride

import com.google.gson.annotations.SerializedName

data class NetworkSearchRide(
    val uuid: String,
    val passenger: NetworkSearchRideDriver,
    @SerializedName("base_amount")
    val baseAmount: Number,
    @SerializedName("updated_amount")
    val updatedAmount: Number?,
    @SerializedName("recommended_amount")
    val recommendedAmount: NetworkRecommendedAmount,
    val locations: NetworkSearchRideLocations,
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
data class NetworkOptions(
    val options: List<NetworkRideOption>
)

data class NetworkDistance(
    val segments: List<NetworkDistanceSegment>,
    @SerializedName("total_distance")
    val totalDistance: Number,
    @SerializedName("total_duration")
    val totalDuration: Number
)

data class NetworkDistanceSegment(
    val distance: Number,
    val duration: Number,
    @SerializedName("start_point")
    val startPoint: NetworkLocationPoint,
    @SerializedName("end_point")
    val endPoint: NetworkLocationPoint
)

data class NetworkRideOption(
    val id: Int,
    val name: String
)

data class NetworkPaymentMethod(
    val id: Int,
    val name: String,
    @SerializedName("is_active")
    val isActive: Boolean
)

data class NetworkSearchRideLocations(
    val points: List<NetworkLocationPoint>
)

data class NetworkLocationPoint(
    val coordinates: NetworkLocationPointCoordinates,
    val name: String
)

data class NetworkLocationPointCoordinates (
    val longitude: Number,
    val latitude: Number
)

data class NetworkSearchRideDriver(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_fullname")
    val userFullName: String,
    @SerializedName("user_rating")
    val userRating: Number
)



data class NetworkRecommendedAmount(
    @SerializedName("min_amount")
    val minAmount: Number,
    @SerializedName("max_amount")
    val maxAmount: Number,
    @SerializedName("recommended_amount")
    val recommendedAmount: Number
)
