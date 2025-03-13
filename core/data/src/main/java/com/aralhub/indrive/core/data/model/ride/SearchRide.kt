package com.aralhub.indrive.core.data.model.ride

import com.aralhub.network.models.location.NetworkLocationPoint
import com.aralhub.network.models.location.NetworkLocationPointCoordinates
import com.aralhub.network.models.location.NetworkLocations

data class SearchRide(
    val uuid: String,
    val passenger: SearchRideDriver,
    val baseAmount: Number,
    val updatedAmount: Number?,
    val recommendedAmount: RecommendedAmount,
    val locations: SearchRideLocations,
    val comment: String,
    val paymentMethod: PaymentMethod,
    val options: List<RideOption>,
    val autoTake: Boolean,
    val distance: Distance,
    val cancelCauseId: Int,
)

data class Distance(
    val segments: List<DistanceSegment>,
    val totalDistance: Number,
    val totalDuration: Number
)

data class DistanceSegment(
    val distance: Number,
    val duration: Number,
    val startPoint: LocationPoint,
    val endPoint: LocationPoint
)

data class RideOption(
    val id: Int,
    val name: String
)

data class PaymentMethod(
    val id: Int,
    val name: String,
    val isActive: Boolean
)

fun NetworkLocations.toDomain() = SearchRideLocations(
    points = points.map { it.toDomain() }
)

data class SearchRideLocations(
    val points: List<LocationPoint>
)

fun NetworkLocationPoint.toDomain() = LocationPoint(
    coordinates = coordinates.toDomain(),
    name = name
)

data class LocationPoint(
    val coordinates: LocationPointCoordinates,
    val name: String
)

fun NetworkLocationPointCoordinates.toDomain() = LocationPointCoordinates(
    longitude = longitude,
    latitude = latitude
)
data class LocationPointCoordinates(
    val longitude: Number,
    val latitude: Number
)

data class SearchRideDriver(
    val userId: Int,
    val userFullName: String,
    val userRating: Number
)

data class RecommendedAmount(
    val minAmount: Number,
    val maxAmount: Number,
    val recommendedAmount: Number
)
