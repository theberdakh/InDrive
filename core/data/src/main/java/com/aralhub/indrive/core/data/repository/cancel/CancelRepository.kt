package com.aralhub.indrive.core.data.repository.cancel

import com.aralhub.indrive.core.data.model.cancel.CancelCause
import com.aralhub.indrive.core.data.result.Result

interface CancelRepository {
    suspend fun getCancelCauses(): Result<List<CancelCause>>
}