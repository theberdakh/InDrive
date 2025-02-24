package com.aralhub.indrive.core.data.repository.client.impl

import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.repository.client.ClientWebSocketRepository
import com.aralhub.indrive.core.data.result.Result
import com.aralhub.network.WebSocketClientNetworkDataSource
import com.aralhub.network.models.NetworkResult
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePoint
import com.aralhub.network.models.websocketclient.NetworkGetRecommendedRidePricePointCoordinates
import javax.inject.Inject

class ClientWebSocketRepositoryImpl @Inject constructor(private val dataSource: WebSocketClientNetworkDataSource) :
    ClientWebSocketRepository {
    override suspend fun getRecommendedPrice(points: List<GeoPoint>): Result<RecommendedPrice> {
       return dataSource.getRecommendedPrice(points.map { NetworkGetRecommendedRidePricePoint(
           coordinates = NetworkGetRecommendedRidePricePointCoordinates(it.longitude, it.latitude),
           name = "point"
       ) }).let {
            when (it) {
                is NetworkResult.Error -> Result.Error(it.message)
                is NetworkResult.Success -> Result.Success(RecommendedPrice(
                    minAmount = it.data.minAmount,
                    maxAmount = it.data.maxAmount,
                    recommendedAmount = it.data.recommendedAmount
                ))
            }
        }
    }
}