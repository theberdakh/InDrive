package com.aralhub.araltaxi.select_location.utils

import kotlin.math.*

// Earth radius in meters
private const val EARTH_RADIUS = 6371000.0

/**
 * Calculates a point at a given distance and bearing from the starting point
 * @param lat Starting latitude in degrees
 * @param lng Starting longitude in degrees
 * @param distanceMeters Distance in meters
 * @param bearingDegrees Bearing in degrees (0 = north, 90 = east, 180 = south, 270 = west)
 * @return Pair of (latitude, longitude) in degrees
 */
fun calculateDestinationPoint(lat: Double, lng: Double, distanceMeters: Double, bearingDegrees: Double): Pair<Double, Double> {
    // Convert to radians
    val latRad = Math.toRadians(lat)
    val lngRad = Math.toRadians(lng)
    val bearingRad = Math.toRadians(bearingDegrees)

    // Angular distance in radians
    val angularDistance = distanceMeters / EARTH_RADIUS

    // Calculate new latitude
    val newLatRad = asin(
        sin(latRad) * cos(angularDistance) +
                cos(latRad) * sin(angularDistance) * cos(bearingRad)
    )

    // Calculate new longitude
    val newLngRad = lngRad + atan2(
        sin(bearingRad) * sin(angularDistance) * cos(latRad),
        cos(angularDistance) - sin(latRad) * sin(newLatRad)
    )

    // Convert back to degrees
    val newLat = Math.toDegrees(newLatRad)
    val newLng = Math.toDegrees(newLngRad)

    return Pair(newLat, newLng)
}

/**
 * Generates four points 100 meters in each cardinal direction from the center
 * @param centerLat Center latitude in degrees
 * @param centerLng Center longitude in degrees
 * @return Map with keys "north", "east", "south", "west" and coordinate pair values
 */
fun generateCardinalPoints(centerLat: Double, centerLng: Double): Map<String, Pair<Double, Double>> {
    val distance = 100.0 // meters

    return mapOf(
        "north" to calculateDestinationPoint(centerLat, centerLng, distance, 0.0),
        "east" to calculateDestinationPoint(centerLat, centerLng, distance, 90.0),
        "south" to calculateDestinationPoint(centerLat, centerLng, distance, 180.0),
        "west" to calculateDestinationPoint(centerLat, centerLng, distance, 270.0)
    )
}

data class LatLon(val lat: Double, val lon: Double) {
    override fun toString(): String = "($lat, $lon)"
}

fun getPointsAt10MeterRadius(centerLat: Double, centerLon: Double): List<LatLon> {
    val earthRadius = 6371000.0 // Earth's radius in meters

    // Approximate meters per degree (varies slightly with latitude)
    val metersPerLatDegree = 111319.9 // 1 degree latitude ≈ 111,319.9 meters
    val metersPerLonDegree = 111319.9 * cos(centerLat.toRadians()) // Adjusts for latitude
    // Distance in degrees for 10 meters
    val latOffset = 10.0 / metersPerLatDegree // 10 meters in latitude
    val lonOffset = 10.0 / metersPerLonDegree // 10 meters in longitude

    // Calculate four points: North, East, South, West
    val points = listOf(
        LatLon(centerLat + latOffset, centerLon),      // 10m North
        LatLon(centerLat, centerLon + lonOffset),      // 10m East
        LatLon(centerLat - latOffset, centerLon),      // 10m South
        LatLon(centerLat, centerLon - lonOffset)       // 10m West
    )

    return points
}

fun Double.toRadians(): Double = this * Math.PI / 180.0

private fun latOffset(meters: Int): Double {
    return (meters / 111111.0) // 1 degree lat ≈ 111111 meters
}

private fun longOffset(meters: Int, latitude: Double): Double {
    return meters / (111111.0 * cos(Math.toRadians(latitude)))
}