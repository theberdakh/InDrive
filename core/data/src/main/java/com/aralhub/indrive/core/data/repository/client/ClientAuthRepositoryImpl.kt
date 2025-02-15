package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.model.client.asDomain
import com.aralhub.indrive.core.data.model.client.toDTO
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class ClientAuthRepositoryImpl @Inject constructor(private val clientNetworkDataSource: UserNetworkDataSource) :
    ClientAuthRepository {
    override suspend fun clientAuth(authRequest: AuthRequest): Result<AuthResponse> {
        clientNetworkDataSource.userAuth(authRequest.toDTO()).let {
            return when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(it.data.asDomain())
            }
        }
    }
}