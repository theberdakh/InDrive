package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.ride.RideStatus
import kotlinx.coroutines.flow.Flow

interface ClientRideRepository {
    suspend fun getRideStatus(): Flow<RideStatus>
    suspend fun close()
}