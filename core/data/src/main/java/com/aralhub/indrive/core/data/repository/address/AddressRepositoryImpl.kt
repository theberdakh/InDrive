package com.aralhub.indrive.core.data.repository.address

import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.AddressNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.address.NetworkAddressRequest
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressNetworkDataSource: AddressNetworkDataSource): AddressRepository {
    override suspend fun createAddress(address: CreateAddressRequest): Result<Address> {
        addressNetworkDataSource.address(NetworkAddressRequest(address.userId, address.name, address.address, address.latitude, address.longitude)).let {
            when(it) {
                is NetworkResult.Success -> {
                    return Result.Success(Address(it.data.id, it.data.userId, it.data.name, it.data.address, it.data.latitude, it.data.longitude))
                }
                is NetworkResult.Error -> {
                    return Result.Error(it.message)
                }
            }
        }
    }
}