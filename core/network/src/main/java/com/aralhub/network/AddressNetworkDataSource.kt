package com.aralhub.network

import com.aralhub.network.models.address.NetworkAddressByIdResponse
import com.aralhub.network.models.address.NetworkAddressByUserIdResponse
import com.aralhub.network.models.address.NetworkAddressRequest
import com.aralhub.network.models.address.NetworkAddressResponse
import com.aralhub.network.models.address.NetworkUpdateAddressResponse

interface AddressNetworkDataSource {
    /** Create address*/
    suspend fun address(networkAddressRequest: NetworkAddressRequest): NetworkAddressResponse
    suspend fun getAddressByUserId(userId: Int): NetworkAddressByUserIdResponse
    suspend fun getAddressById(addressId: Int): NetworkAddressByIdResponse
    suspend fun updateAddress(addressId: Int): NetworkUpdateAddressResponse
    suspend fun deleteAddress(addressId: Int)
}