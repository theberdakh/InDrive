package com.aralhub.network.impl

import com.aralhub.network.AddressNetworkDataSource
import com.aralhub.network.api.AddressNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.address.NetworkAddress
import com.aralhub.network.models.address.NetworkDeletedAddress
import com.aralhub.network.requests.address.NetworkAddressRequest
import com.aralhub.network.utils.ex.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class AddressNetworkDataSourceImpl @Inject constructor(private val api: AddressNetworkApi) :
    AddressNetworkDataSource {
    override suspend fun address(networkAddressRequest: NetworkAddressRequest): NetworkResult<NetworkAddress> {
        return api.address(networkAddressRequest).safeRequestServerResponse()
    }

    override suspend fun getAddressByUserId(userId: Int): NetworkResult<List<NetworkAddress>> {
        return api.getAddressByUserId(userId).safeRequestServerResponse()
    }

    override suspend fun getAddressById(addressId: Int): NetworkResult<NetworkAddress> {
        return api.getAddressById(addressId).safeRequestServerResponse()
    }

    override suspend fun updateAddress(
        addressId: Int,
        networkAddressRequest: NetworkAddressRequest
    ): NetworkResult<NetworkAddress> {
        return api.updateAddress(addressId, networkAddressRequest).safeRequestServerResponse()
    }

    override suspend fun deleteAddress(addressId: Int): NetworkResult<NetworkDeletedAddress> {
        return api.deleteAddress(addressId).safeRequestServerResponse()
    }

}
