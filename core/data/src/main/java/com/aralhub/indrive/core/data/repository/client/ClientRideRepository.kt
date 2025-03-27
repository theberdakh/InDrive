package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.ride.RideStatus
import com.aralhub.indrive.core.data.model.ride.WaitAmount
import com.aralhub.indrive.core.data.result.Result
import kotlinx.coroutines.flow.SharedFlow

interface ClientRideRepository {
    suspend fun getRideStatus(): SharedFlow<RideStatus>
    suspend fun close()
}