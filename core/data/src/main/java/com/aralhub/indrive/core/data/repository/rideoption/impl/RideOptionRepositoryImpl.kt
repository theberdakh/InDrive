package com.aralhub.indrive.core.data.repository.rideoption.impl

import com.aralhub.indrive.core.data.model.rideoption.RideOption
import com.aralhub.indrive.core.data.repository.rideoption.RideOptionRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.RideOptionNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class RideOptionRepositoryImpl @Inject constructor(private val rideOptionNetworkDataSource: RideOptionNetworkDataSource): RideOptionRepository {
    override suspend fun getRideOptions(): Result<List<RideOption>> {
        return rideOptionNetworkDataSource.getRideOptions().let {
            when(it){
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(it.data.map {
                    networkRideOption ->
                    RideOption(
                        id = networkRideOption.id,
                        name = networkRideOption.name)
                })
            }
        }
    }
}