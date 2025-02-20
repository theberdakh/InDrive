package com.aralhub.indrive.core.data.repository.client

import com.aralhub.indrive.core.data.model.client.AuthRequest
import com.aralhub.indrive.core.data.model.client.AuthResponse
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.indrive.core.data.model.client.ClientVerifyResponse
import com.aralhub.indrive.core.data.model.client.asDomain
import com.aralhub.indrive.core.data.model.client.toDTO
import com.aralhub.indrive.core.data.result.WrappedResult
import com.aralhub.network.UserNetworkDataSource
import com.aralhub.network.models.NetworkWrappedResult
import javax.inject.Inject

class ClientAuthRepositoryImpl @Inject constructor(private val clientNetworkDataSource: UserNetworkDataSource) :
    ClientAuthRepository {
    override suspend fun clientAuth(authRequest: AuthRequest): WrappedResult<AuthResponse> {
        clientNetworkDataSource.userAuth(authRequest.toDTO()).let {
            return when (it) {
                is NetworkWrappedResult.Error -> WrappedResult.Error(it.message)
                is NetworkWrappedResult.Loading -> WrappedResult.Loading
                is NetworkWrappedResult.Success -> WrappedResult.Success(it.data.asDomain())
            }
        }
    }

    override suspend fun userVerify(networkUserVerifyRequest: ClientVerifyRequest): WrappedResult<ClientVerifyResponse> {
        clientNetworkDataSource.userVerify(networkUserVerifyRequest.toDTO()).let {
            return when (it) {
                is NetworkWrappedResult.Error -> WrappedResult.Error(it.message)
                is NetworkWrappedResult.Loading -> WrappedResult.Loading
                is NetworkWrappedResult.Success -> WrappedResult.Success(it.data.data.asDomain())
            }
        }
    }
}