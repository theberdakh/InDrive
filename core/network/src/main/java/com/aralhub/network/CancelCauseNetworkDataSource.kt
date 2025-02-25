package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.cancel.NetworkCancelCause

interface CancelCauseNetworkDataSource {
    suspend fun getCancelCauses(): NetworkResult<List<NetworkCancelCause>>
}