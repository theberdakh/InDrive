package com.aralhub.indrive.core.data.repository.cancel.impl

import com.aralhub.indrive.core.data.model.cancel.CancelCause
import com.aralhub.indrive.core.data.repository.cancel.CancelRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.CancelCauseNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class CancelRepositoryImpl @Inject constructor(private val dataSource: CancelCauseNetworkDataSource) :
    CancelRepository {
    override suspend fun getCancelCauses(): Result<List<CancelCause>> {
        return dataSource.getCancelCauses().let {
            when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(it.data.map { cancelCause ->
                    CancelCause(
                        name = cancelCause.name,
                        type = cancelCause.type,
                        isActive = cancelCause.isActive,
                        id = cancelCause.id
                    )
                })
            }
        }
    }
}