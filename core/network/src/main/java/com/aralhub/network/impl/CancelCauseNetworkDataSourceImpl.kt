package com.aralhub.network.impl

import com.aralhub.network.CancelCauseNetworkDataSource
import com.aralhub.network.api.CancelCauseNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.cancel.NetworkCancelCause
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class CancelCauseNetworkDataSourceImpl @Inject constructor(private val api: CancelCauseNetworkApi): CancelCauseNetworkDataSource {
    override suspend fun getCancelCauses(): NetworkResult<List<NetworkCancelCause>> {
        return api.getCancelCauses().safeRequestServerResponse()
    }
}