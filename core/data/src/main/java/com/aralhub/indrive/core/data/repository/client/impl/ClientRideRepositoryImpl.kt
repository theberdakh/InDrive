package com.aralhub.indrive.core.data.repository.client.impl

import com.aralhub.indrive.core.data.model.ride.RideStatus
import com.aralhub.indrive.core.data.repository.client.ClientRideRepository
import com.aralhub.network.ClientRideNetworkDataSource
import com.aralhub.network.models.ride.NetworkRideStatus
import com.aralhub.network.utils.ClientWebSocketEventRide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClientRideRepositoryImpl @Inject constructor(private val clientRideNetworkDataSource: ClientRideNetworkDataSource): ClientRideRepository {
    override suspend fun getRideStatus(): Flow<RideStatus> {
        return clientRideNetworkDataSource.getRide().map {
            when(it){
                is ClientWebSocketEventRide.RideUpdate -> {
                    when(it.networkRideStatus.status) {
                        NetworkRideStatus.DRIVER_ON_THE_WAY-> RideStatus.DriverOnTheWay(it.networkRideStatus.message)
                        NetworkRideStatus.RIDE_COMPLETED-> RideStatus.RideCompleted(it.networkRideStatus.message)
                        NetworkRideStatus.RIDE_STARTED_AFTER_WAITING-> RideStatus.RideStartedAfterWaiting(it.networkRideStatus.message)
                        NetworkRideStatus.CANCELED_BY_DRIVER-> RideStatus.CanceledByDriver(it.networkRideStatus.message)
                        NetworkRideStatus.DRIVER_WAITING_CLIENT-> RideStatus.DriverWaitingClient(it.networkRideStatus.message)
                        NetworkRideStatus.RIDE_STARTED-> RideStatus.RideStarted(it.networkRideStatus.message)
                        else -> RideStatus.Unknown(it.networkRideStatus.message)
                    }
                }
                is ClientWebSocketEventRide.Unknown -> {
                    RideStatus.Unknown(it.error)
                }
            }
        }
    }

    override suspend fun close() {
        clientRideNetworkDataSource.close()
    }

}