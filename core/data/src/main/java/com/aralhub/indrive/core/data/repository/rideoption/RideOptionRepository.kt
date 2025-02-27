package com.aralhub.indrive.core.data.repository.rideoption

import com.aralhub.indrive.core.data.model.rideoption.RideOption
import com.aralhub.indrive.core.data.result.Result

interface RideOptionRepository {
    suspend fun getRideOptions(): Result<List<RideOption>>
}