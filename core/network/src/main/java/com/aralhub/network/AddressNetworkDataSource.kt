package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.address.NetworkAddress
import com.aralhub.network.models.address.NetworkDeletedAddress
import com.aralhub.network.requests.address.NetworkAddressRequest

interface AddressNetworkDataSource {
    suspend fun address(networkAddressRequest: NetworkAddressRequest): NetworkResult<NetworkAddress>
    suspend fun getAddressByUserId(userId: Int): NetworkResult<List<NetworkAddress>>
    suspend fun getAddressById(addressId: Int): NetworkResult<NetworkAddress>
    suspend fun updateAddress(addressId: Int, networkAddressRequest: NetworkAddressRequest): NetworkResult<NetworkAddress>
    suspend fun deleteAddress(addressId: Int): NetworkResult<NetworkDeletedAddress>
}