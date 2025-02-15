package com.aralhub.indrive.core.data.repository.client.impl

import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyPhoneRequest
import com.aralhub.indrive.core.data.model.client.toNetwork
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import javax.inject.Inject

class ClientAuthRepositoryImpl @Inject constructor(private val clientNetworkDataSource: UserNetworkDataSource) :
    ClientAuthRepository {
    override suspend fun clientAddPhone(clientAddPhoneRequest: ClientAddPhoneRequest): Result<Boolean> {
        clientNetworkDataSource.userAuth(networkUserAuthRequest = clientAddPhoneRequest.toNetwork()).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(it.data)
            }
        }
    }

    override suspend fun clientAuthVerify(clientVerifyPhoneRequest: ClientVerifyPhoneRequest): Result<Boolean> {
        clientNetworkDataSource.userVerify(networkUserVerifyRequest = NetworkUserVerifyRequest(clientVerifyPhoneRequest.phone, clientVerifyPhoneRequest.code)).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(data = true)
            }
        }
    }
}