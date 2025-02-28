package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.ServerResponse
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
import com.aralhub.network.models.ServerResponseEmpty
import com.aralhub.network.models.auth.NetworkAuthToken
import com.aralhub.network.models.balance.NetworkBalance
import com.aralhub.network.models.card.NetworkCard
import com.aralhub.network.models.driver.NetworkDriverActive
import com.aralhub.network.models.driver.NetworkDriverInfo
import com.aralhub.network.requests.auth.NetworkDriverAuthRequest
import com.aralhub.network.requests.logout.NetworkLogoutRequest
import com.aralhub.network.requests.verify.NetworkVerifyRequest
import java.io.File

interface DriverNetworkDataSource {
    suspend fun driverAuth(networkDriverAuthRequest: NetworkDriverAuthRequest): NetworkResult<String>
    suspend fun driverVerify(networkDriverVerifyRequest: NetworkVerifyRequest): NetworkResult<NetworkAuthToken>
    suspend fun getDriverVehicle(): NetworkResult<String>
    suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfo>
    suspend fun getDriverInfoWithVehicle(): NetworkResult<NetworkDriverActive>
    suspend fun driverCard(networkDriverCardRequest: NetworkCard): NetworkResult<Boolean>
    suspend fun getDriverBalance(): NetworkResult<NetworkBalance>
    suspend fun getDriverCard(): NetworkResult<NetworkCard>
    suspend fun driverLogout(networkDriverLogoutRequest: NetworkLogoutRequest): NetworkResult<Boolean>
    suspend fun driverPhoto(file: File): NetworkResult<ServerResponseEmpty>
    suspend fun getDriverInfo(): NetworkResult<NetworkDriverInfoResponse>
    suspend fun getDriverInfoWithVehicle(): NetworkResult<DriverInfoWithVehicleResponse>
    suspend fun driverCard(networkDriverCardRequest: NetworkDriverCardRequest): NetworkResult<Boolean>
    suspend fun getDriverBalance(): NetworkResult<NetworkDriverBalanceResponse>
    suspend fun getDriverCard(): NetworkResult<NetworkDriverCardResponse>
    suspend fun driverLogout(networkDriverLogoutRequest: NetworkDriverLogoutRequest): NetworkResult<Boolean>
    suspend fun getActiveRides(sendLocationRequest: NetworkSendLocationRequestWithoutType): NetworkResult<List<WebSocketServerResponse<NetworkActiveOfferResponse>>>
}