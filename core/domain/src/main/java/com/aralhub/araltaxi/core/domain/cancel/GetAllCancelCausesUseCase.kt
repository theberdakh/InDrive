package com.aralhub.araltaxi.core.domain.cancel

import com.aralhub.indrive.core.data.repository.cancel.CancelRepository
import javax.inject.Inject

class GetAllCancelCausesUseCase @Inject constructor(private val repository: CancelRepository) {
    suspend operator fun invoke() = repository.getCancelCauses()
}