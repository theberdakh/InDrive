package com.aralhub.network.impl

import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.api.DriverNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.auth.NetworkAuthToken
import com.aralhub.network.models.balance.NetworkBalance
import com.aralhub.network.models.card.NetworkCard
import com.aralhub.network.models.driver.NetworkDriverActive
import com.aralhub.network.models.driver.NetworkDriverInfo
import com.aralhub.network.requests.auth.NetworkDriverAuthRequest
import com.aralhub.network.requests.logout.NetworkLogoutRequest
import com.aralhub.network.requests.verify.NetworkVerifyRequest
import com.aralhub.network.utils.ex.MultipartEx
import com.aralhub.network.utils.ex.NetworkEx.safeRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequestEmpty
import com.aralhub.network.utils.ex.NetworkEx.safeRequestServerResponse
import java.io.File
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

    override suspend fun driverVerify(networkDriverVerifyRequest: NetworkVerifyRequest): NetworkResult<NetworkAuthToken> {
        return api.driverVerify(networkDriverVerifyRequest).safeRequestServerResponse()
    }

    override suspend fun getDriverVehicle(): NetworkResult<String> {
        return api.getDriverVehicle().safeRequestServerResponse()
    }

    override suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfo> {
        return api.getDriverInfo().safeRequestServerResponse()
    }

    override suspend fun getDriverInfoWithVehicle(): NetworkResult<NetworkDriverActive> {
        return api.getDriverInfoWithVehicle().safeRequestServerResponse()
    }


    override suspend fun driverCard(networkDriverCardRequest: NetworkCard): NetworkResult<Boolean> {
        return api.driverCard(networkDriverCardRequest).safeRequestEmpty()
    }

    override suspend fun getDriverBalance(): NetworkResult<NetworkBalance> {
        return api.getDriverBalance().safeRequestServerResponse()
    }

    override suspend fun getDriverCard(): NetworkResult<NetworkCard> {
        return api.getDriverCard().safeRequestServerResponse()
    }

    override suspend fun driverLogout(networkDriverLogoutRequest: NetworkLogoutRequest): NetworkResult<Boolean> {
        return api.driverLogout(networkDriverLogoutRequest).safeRequestEmpty()
    }

    override suspend fun driverPhoto(file: File): NetworkResult<ServerResponseEmpty> {
        return api.driverPhoto(MultipartEx.getMultipartFromFile(file)).safeRequest()
    }

    override suspend fun getActiveRides(sendLocationRequest: NetworkSendLocationRequestWithoutType): NetworkResult<List<WebSocketServerResponse<NetworkActiveOfferResponse>>> {
        return api.getActiveRides(sendLocationRequest).safeRequestServerResponse()
    }
}