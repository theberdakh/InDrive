package com.aralhub.araltaxi.core.domain.address

import com.aralhub.indrive.core.data.repository.address.AddressRepository
import com.aralhub.indrive.core.data.result.Result
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(addressId: Int): Result<Boolean> {
        return addressRepository.deleteAddress(addressId)
    }
}