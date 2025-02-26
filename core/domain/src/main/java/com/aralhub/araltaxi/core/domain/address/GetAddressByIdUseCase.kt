package com.aralhub.araltaxi.core.domain.address

import com.aralhub.indrive.core.data.repository.address.AddressRepository
import javax.inject.Inject

class GetAddressByIdUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(addressId: Int) = addressRepository.getAddressById(addressId)
}