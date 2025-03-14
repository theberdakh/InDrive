package com.aralhub.indrive.core.data.repository.address

import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result

interface AddressRepository {
    suspend fun createAddress(createAddressRequest: CreateAddressRequest): Result<Address>
    suspend fun getAllAddresses(userId: Int): Result<List<Address>>
    suspend fun getAddressById(addressId: Int): Result<Address>
    suspend fun updateAddress(addressId: Int, createAddressRequest: CreateAddressRequest): Result<Address>
    suspend fun deleteAddress(addressId: Int): Result<Boolean>
}