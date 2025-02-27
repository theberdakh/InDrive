package com.aralhub.indrive.core.data.model.client



data class ClientRideRequest(
    val passengerId: Int,
    val baseAmount: Number,
    val recommendedAmount: ClientRideRequestRecommendedAmount,
    val locations: ClientRideRequestLocations,
    val comment: String,
    val autoTake: Boolean,
    val paymentId: Int,
    val optionIds: List<Int>,
    val cancelCauseId: Int? = null
)

data class ClientRideRequestRecommendedAmount(
    val minAmount: Number,
    val maxAmount: Number,
    val recommendedAmount: Number
)

data class ClientRideRequestLocations(
    val points: List<ClientRideRequestLocationsItems>
)

data class ClientRideRequestLocationsItems(
    val coordinates: ClientRideRequestLocationsItemsCoordinates,
    val name: String
)

data class ClientRideRequestLocationsItemsCoordinates(
    val longitude: Number,
    val latitude: Number
)

data class ClientRide(
    val uuid: String,
    val passenger: ClientRideResponsePassenger,
    val baseAmount: Number,
    val updatedAmount: Number? = null,
    val recommendedAmount: ClientRideResponseRecommendedAmount,
    val locations: ClientRideResponseLocations,
    val comment: String,
    val paymentMethod: ClientRideResponsePaymentMethod,
    val options: ClientRideResponseOptions,
    val autoTake: Boolean,
    val distance: ClientRideResponseDistance,
    val cancelCauseId: Int? = null
)

data class ClientRideResponseRecommendedAmount(
    val minAmount: Number,
    val maxAmount: Number,
    val recommendedAmount: Number
)

data class ClientRideResponseDistance(
    val segments: List<ClientRideResponseDistanceItem>,
    val totalDistance: Number,
    val totalDuration: Number)

data class ClientRideResponseDistanceItem(
    val distance: Number,
    val duration: Number,
    val startPoint: ClientRideResponseDistanceItemPoint,
    val endPoint: ClientRideResponseDistanceItemPoint,
)

data class ClientRideResponseDistanceItemPoint(
    val coordinates: ClientRideResponseDistanceItemStartPointCoordinates,
    val name: String
)

/**
 * @param longitude -180, 180
 * @param latitude -90, 90
 * */
data class ClientRideResponseDistanceItemStartPointCoordinates(
    val longitude: Number,
    val latitude: Number
)

data class ClientRideResponseOptions(
    val options: List<ClientRideResponseOptionsItem>
)

data class ClientRideResponseOptionsItem(
    val id: Int,
    val name: String
)

data class ClientRideResponsePaymentMethod(
    val id: Int,
    val name: String,
    val isActive: Boolean
)

data class ClientRideResponsePassenger(
    val userId: Int,
    val userFullName: String,
    val userRating: Number
)

data class ClientRideResponseLocations(
    val points: List<ClientRideResponseLocationsItems>
)

data class ClientRideResponseLocationsItems(
    val coordinates: ClientRideResponseLocationsItemsCoordinates,
    val name: String
)

data class ClientRideResponseLocationsItemsCoordinates(
    val longitude: Number,
    val latitude: Number
)


