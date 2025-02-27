package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.ClientRide
import com.aralhub.indrive.core.data.model.client.ClientRideRequest
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.result.Result


interface ClientWebSocketRepository {
    suspend fun getRecommendedPrice(points: List<GeoPoint>): Result<RecommendedPrice>
    suspend fun createRide(clientRideRequest: ClientRideRequest): Result<ClientRide>
    suspend fun getActiveRideByPassenger(userId: Int): Result<ActiveRide>
    suspend fun getSearchRideByPassengerId(userId: Int): Result<SearchRide>
}