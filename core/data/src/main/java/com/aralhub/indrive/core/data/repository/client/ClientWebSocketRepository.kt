package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.ClientRide
import com.aralhub.indrive.core.data.model.client.ClientRideRequest
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.model.driver.DriverCard
import com.aralhub.indrive.core.data.model.ride.ActiveRide
import com.aralhub.indrive.core.data.model.ride.SearchRide
import com.aralhub.indrive.core.data.model.ride.StandardPrice
import com.aralhub.indrive.core.data.model.ride.WaitAmount
import com.aralhub.indrive.core.data.result.Result
import java.sql.Driver


interface ClientWebSocketRepository {
    suspend fun getRecommendedPrice(points: List<GeoPoint>): Result<RecommendedPrice>
    suspend fun createRide(clientRideRequest: ClientRideRequest): Result<ClientRide>
    suspend fun getActiveRideByPassenger(): Result<ActiveRide>
    suspend fun getSearchRideByPassengerId(): Result<SearchRide>
    suspend fun cancelSearchRide(rideId: String): Result<Boolean>
    suspend fun updateSearchRideAmount(rideId: String, amount: Number): Result<Boolean>
    suspend fun updateAutoTake(rideId: String, autoTake: Boolean): Result<Boolean>
    suspend fun cancelRide(rideId: Int, cancelCauseId: Int): Result<Boolean>
    suspend fun cancelRideByPassenger(rideId: Int): Result<Boolean>
    suspend fun getWaitAmount(rideId: Int): Result<WaitAmount>
    suspend fun getStandardPrice(): Result<StandardPrice>
    suspend fun getDriverCard(driverId: Int): Result<DriverCard>
}