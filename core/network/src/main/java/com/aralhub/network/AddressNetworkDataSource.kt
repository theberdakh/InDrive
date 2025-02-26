package com.aralhub.network

import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.address.NetworkAddressByIdResponse
import com.aralhub.network.models.address.NetworkAddressByUserIdResponse
import com.aralhub.network.models.address.NetworkAddressRequest
import com.aralhub.network.models.address.NetworkAddressResponse
import com.aralhub.network.models.address.NetworkDeleteAddressResponse
import com.aralhub.network.models.address.NetworkUpdateAddressResponse

interface AddressNetworkDataSource {
    /** Create address*/
    suspend fun address(networkAddressRequest: NetworkAddressRequest): NetworkResult<NetworkAddressResponse>
    suspend fun getAddressByUserId(userId: Int): NetworkResult<List<NetworkAddressByUserIdResponse>>
    suspend fun getAddressById(addressId: Int): NetworkResult<NetworkAddressByIdResponse>
    suspend fun updateAddress(addressId: Int): NetworkResult<NetworkUpdateAddressResponse>
    suspend fun deleteAddress(addressId: Int): NetworkResult<NetworkDeleteAddressResponse>
}