package com.aralhub.network.impl

import com.aralhub.network.AddressNetworkDataSource
import com.aralhub.network.api.AddressNetworkApi
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.address.NetworkAddressByIdResponse
import com.aralhub.network.models.address.NetworkAddressByUserIdResponse
import com.aralhub.network.models.address.NetworkAddressRequest
import com.aralhub.network.models.address.NetworkAddressResponse
import com.aralhub.network.models.address.NetworkDeleteAddressResponse
import com.aralhub.network.models.address.NetworkUpdateAddressResponse
import com.aralhub.network.utils.NetworkEx.safeRequestServerResponse
import javax.inject.Inject

class AddressNetworkDataSourceImpl @Inject constructor(private val api: AddressNetworkApi) :
    AddressNetworkDataSource {
    override suspend fun address(networkAddressRequest: NetworkAddressRequest): NetworkResult<NetworkAddressResponse> {
        return api.address(networkAddressRequest).safeRequestServerResponse()
    }

    override suspend fun getAddressByUserId(userId: Int): NetworkResult<List<NetworkAddressByUserIdResponse>> {
        return api.getAddressByUserId(userId).safeRequestServerResponse()
    }

    override suspend fun getAddressById(addressId: Int): NetworkResult<NetworkAddressByIdResponse> {
        return api.getAddressById(addressId).safeRequestServerResponse()
    }

    override suspend fun updateAddress(
        addressId: Int,
        networkAddressRequest: NetworkAddressRequest
    ): NetworkResult<NetworkUpdateAddressResponse> {
        return api.updateAddress(addressId, networkAddressRequest).safeRequestServerResponse()
    }

    override suspend fun deleteAddress(addressId: Int): NetworkResult<NetworkDeleteAddressResponse> {
        return api.deleteAddress(addressId).safeRequestServerResponse()
    }

}
