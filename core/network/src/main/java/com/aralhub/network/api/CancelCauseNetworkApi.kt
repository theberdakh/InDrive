package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.cancel.NetworkCancelCause
import retrofit2.Response
import retrofit2.http.GET

interface CancelCauseNetworkApi {
    @GET("/cancel_cause/")
    suspend fun getCancelCauses(): Response<ServerResponse<List<NetworkCancelCause>>>
}