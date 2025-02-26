package com.aralhub.araltaxi.core.domain.address

import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.indrive.core.data.repository.address.AddressRepository
import javax.inject.Inject

class CreateAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(address: CreateAddressRequest) =
        addressRepository.createAddress(address)
}