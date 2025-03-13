package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.model.location.SendLocationRequest
import com.aralhub.indrive.core.data.model.location.toDTO
import com.aralhub.indrive.core.data.model.offer.toDomain
import com.aralhub.indrive.core.data.repository.driver.DriverWebSocketRepository
import com.aralhub.indrive.core.data.util.StartedRideWebSocketEvent
import com.aralhub.indrive.core.data.util.WebSocketEvent
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.utils.StartedRideWebSocketEventNetwork
import com.aralhub.network.utils.WebSocketEventNetwork
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DriverWebSocketRepositoryImpl @Inject constructor(
    private val driverNetworkDataSource: WebSocketDriverNetworkDataSource
) : DriverWebSocketRepository {
    override fun getActiveRides() = driverNetworkDataSource.getActiveOrders().map {
        when (it) {
            is WebSocketEventNetwork.ActiveOffer -> {
                WebSocketEvent.ActiveOffer(it.offer.toDomain())
            }

            is WebSocketEventNetwork.OfferAccepted -> {
                WebSocketEvent.OfferAccepted(it.rideId)
            }

            is WebSocketEventNetwork.OfferReject -> {
                WebSocketEvent.OfferReject(it.rideUUID)
            }

            is WebSocketEventNetwork.RideCancel -> {
                WebSocketEvent.RideCancel(it.rideId)
            }

            is WebSocketEventNetwork.Unknown -> {
                WebSocketEvent.Unknown(it.error)
            }
        }
    }

    override fun getStartedRideStatus() = driverNetworkDataSource.getStartedRideStatus().map {
        when (it) {
            is StartedRideWebSocketEventNetwork.RideCancelledByPassenger -> {
                StartedRideWebSocketEvent.RideCancelledByPassenger
            }

            is StartedRideWebSocketEventNetwork.UnknownAction -> {
                StartedRideWebSocketEvent.UnknownAction(it.error)
            }
        }
    }

    override suspend fun sendLocation(data: SendLocationRequest) {
        driverNetworkDataSource.sendLocation(
            data.toDTO()
        )
    }

    override suspend fun close() {
        driverNetworkDataSource.close()
    }
}