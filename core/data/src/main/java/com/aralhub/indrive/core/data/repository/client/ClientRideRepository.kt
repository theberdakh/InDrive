package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.ride.RideStatus
import kotlinx.coroutines.flow.SharedFlow

interface ClientRideRepository {
    suspend fun getRideStatus(): SharedFlow<RideStatus>
    suspend fun close()
}