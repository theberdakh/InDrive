package com.aralhub.network.impl

import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.driver.DriverInfoWithVehicleResponse
import com.aralhub.network.models.driver.NetworkDriverAuthRequest
import com.aralhub.network.models.driver.NetworkDriverBalanceResponse
import com.aralhub.network.models.driver.NetworkDriverCardRequest
import com.aralhub.network.models.driver.NetworkDriverCardResponse
import com.aralhub.network.models.driver.NetworkDriverInfoResponse
import com.aralhub.network.models.driver.NetworkDriverLogoutRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyRequest
import com.aralhub.network.models.driver.NetworkDriverVerifyResponse
import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.models.location.NetworkSendLocationRequestWithoutType
import com.aralhub.network.models.offer.NetworkActiveOfferResponse
import com.aralhub.network.utils.NetworkEx.safeRequest
import com.aralhub.network.utils.NetworkEx.safeRequestEmpty
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class DriverNetworkDataSourceImpl @Inject constructor(private val api: DriverNetworkApi): DriverNetworkDataSource {
    override suspend fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String> {
        return  api.driverAuth(networkDriverAuthRequest).safeRequestServerResponse()
    }

    override suspend fun driverVerify(networkDriverVerifyRequest: NetworkDriverVerifyRequest): NetworkResult<NetworkDriverVerifyResponse> {
        return api.driverVerify(networkDriverVerifyRequest).safeRequestServerResponse()
    }

    override suspend fun getDriverVehicle(): NetworkResult<String> {
        return api.getDriverVehicle().safeRequestServerResponse()
    }

    override suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfoResponse> {
        return api.getDriverInfo().safeRequestServerResponse()
    }

    override suspend fun getDriverInfoWithVehicle(): NetworkResult<DriverInfoWithVehicleResponse> {
        return api.getDriverInfoWithVehicle().safeRequestServerResponse()
    }


    override suspend fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<Boolean> {
        return api.driverCard(networkDriverCardRequest).safeRequestEmpty()
    }

    override suspend fun getDriverBalance(): NetworkResult<NetworkDriverBalanceResponse> {
        return api.getDriverBalance().safeRequestServerResponse()
    }

    override suspend fun getDriverCard(): NetworkResult<NetworkDriverCardResponse> {
        return api.getDriverCard().safeRequestServerResponse()
    }

    override suspend fun driverLogout(networkDriverLogoutRequest: NetworkDriverLogoutRequest): NetworkResult<Boolean> {
        return api.driverLogout(networkDriverLogoutRequest).safeRequestEmpty()
    }

    override suspend fun getActiveRides(sendLocationRequest: NetworkSendLocationRequestWithoutType): NetworkResult<List<WebSocketServerResponse<NetworkActiveOfferResponse>>> {
        return api.getActiveRides(sendLocationRequest).safeRequestServerResponse()
    }
}