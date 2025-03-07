package com.aralhub.indrive.core.data.repository.client.impl

import android.util.Log
import com.aralhub.indrive.core.data.model.offer.Offer
import com.aralhub.indrive.core.data.repository.client.ClientOffersRepository
import com.aralhub.indrive.core.data.util.ClientWebSocketEvent
import com.aralhub.network.ClientOffersNetworkDataSource
import com.aralhub.network.utils.ClientWebSocketEventNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ClientOffersRepositoryImpl @Inject constructor(private val clientOffersNetworkDataSource: ClientOffersNetworkDataSource): ClientOffersRepository {

    override suspend fun getOffers(): Flow<ClientWebSocketEvent> {
        return clientOffersNetworkDataSource.getOffers().map {
            Log.i("ClientOffersRepositoryImpl", "getOffers: $it")
            when (it){
                is ClientWebSocketEventNetwork.DriverOffer -> ClientWebSocketEvent.DriverOffer(Offer(
                    offerId = it.offer.offerId,
                    rideUuid = it.offer.rideUuid,
                    driver = it.offer.driver,
                    amount = it.offer.amount,
                    createdAt = it.offer.createdAt,
                    expiresAt = it.offer.expiresAt,
                ))
                is ClientWebSocketEventNetwork.Unknown -> ClientWebSocketEvent.Unknown(it.error)
            }
        }
    }

    override suspend fun close() {
       clientOffersNetworkDataSource.close()
    }
}