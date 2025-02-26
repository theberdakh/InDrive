package com.aralhub.indrive.core.data.repository.address

import com.aralhub.indrive.core.data.model.address.Address
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.result.Result

interface AddressRepository {
    suspend fun createAddress(address: CreateAddressRequest): Result<Address>
    suspend fun getAllAddresses(userId: Int): Result<List<Address>>
}