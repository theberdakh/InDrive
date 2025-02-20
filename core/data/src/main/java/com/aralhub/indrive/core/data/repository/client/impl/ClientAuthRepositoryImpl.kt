package com.aralhub.indrive.core.data.repository.client.impl

import com.aralhub.indrive.core.data.model.client.ClientAddPhoneRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.repository.client.ClientAuthRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.user.NetworkUserAuthRequest
import com.aralhub.network.models.user.NetworkUserProfileRequest
import com.aralhub.network.models.user.NetworkUserVerifyRequest
import com.aralhub.network.utils.LocalStorage
import javax.inject.Inject

class ClientAuthRepositoryImpl @Inject constructor(private val localStorage: LocalStorage, private val clientNetworkDataSource: UserNetworkDataSource) :
    ClientAuthRepository {

    override suspend fun clientAuth(authRequest: ClientAddPhoneRequest): Result<Boolean> {
        clientNetworkDataSource.userAuth(NetworkUserAuthRequest(authRequest.phoneNumber)).let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success ->  Result.Success(data = true)
            }
        }
    }

    override suspend fun userVerify(networkUserVerifyRequest: ClientVerifyRequest): Result<Boolean> {
        clientNetworkDataSource.userVerify(NetworkUserVerifyRequest(networkUserVerifyRequest.phoneNumber, networkUserVerifyRequest.code)).let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> {
                    localStorage.access = it.data.token
                    localStorage.refresh = it.data.refreshToken
                    localStorage.isLogin = true
                    Result.Success(data = true)
                }
            }
        }
    }

    override suspend fun userProfile(fullName: String): Result<Boolean> {
        clientNetworkDataSource.userProfile(NetworkUserProfileRequest(fullName)).let {
            return when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(data = true)
            }
        }
    }
}