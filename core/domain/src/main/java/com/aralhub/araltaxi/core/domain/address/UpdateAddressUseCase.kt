package com.aralhub.araltaxi.core.domain.address

import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.repository.address.AddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(addressId: Int, address: CreateAddressRequest) =
        addressRepository.updateAddress(addressId, address)
}