package com.aralhub.indrive.core.data.repository.driver.impl

import com.aralhub.indrive.core.data.repository.driver.DriverRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.DriverNetworkDataSource
import com.aralhub.network.models.NetworkResult
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val driverNetworkDataSource: DriverNetworkDataSource
) : DriverRepository {
    override suspend fun getActiveRide(): Result<Int?> {
        driverNetworkDataSource.getActiveRide().let {
            return when (it) {
                is NetworkResult.Error -> {
                    Result.Error(it.message)
                }

                is NetworkResult.Success -> {
                    Result.Success(it.data?.id)
                }
            }
        }
    }
    override suspend fun cancelRide(rideId: Int, cancelCauseId: Int): Result<Boolean> {
        driverNetworkDataSource.cancelRide(rideId, cancelCauseId).let {
            return when (it) {
                is NetworkResult.Error -> {
                    Result.Error(it.message)
                }

                is NetworkResult.Success -> {
                    Result.Success(it.data)
                }
            }
        }
    }

    override suspend fun updateRideStatus(rideId: Int, status: String): Result<Boolean> {
        driverNetworkDataSource.updateRideStatus(rideId, status).let {
            return when (it) {
                is NetworkResult.Error -> {
                    Result.Error(it.message)
                }

                is NetworkResult.Success -> {
                    Result.Success(it.data)
                }
            }
        }
    }
}