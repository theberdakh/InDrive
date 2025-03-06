package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.models.offer.NetworkActiveOfferResponse
import com.aralhub.network.models.offer.NetworkOfferCancelResponse
import com.aralhub.network.models.offer.NetworkOfferRejectedResponse
import com.aralhub.network.utils.WebSocketEventNetwork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.isActive

class WebSocketDriverNetworkDataSourceImpl(
    private val client: HttpClient
) : WebSocketDriverNetworkDataSource {

    private var session: WebSocketSession? = null

    override fun getActiveOrders(): Flow<WebSocketEventNetwork> {
        return flow {
            session = client.webSocketSession {
                url("ws://araltaxi.aralhub.uz/websocket/wb/driver")
            }
            if (session?.isActive == true) {
                Log.d("WebSocketLog", "Connected")
            }
            val messageStates = session
                ?.incoming
                ?.consumeAsFlow()
                ?.filterIsInstance<Frame.Text>()
                ?.mapNotNull { frame ->
                    val jsonString = frame.readText()
                    Log.d("WebSocketLog", jsonString)
                    try {
                        val baseResponse = Gson().fromJson(jsonString, WebSocketServerResponse::class.java)

                        when (baseResponse.type) {
                            RIDE_CANCELED -> {
                                val rideCancelData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkOfferCancelResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkOfferCancelResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.RideCancel(rideCancelData.data.rideId)
                            }

                            OFFER_REJECTED -> {
                                val rideCancelData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkOfferRejectedResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkOfferRejectedResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.OfferReject(rideCancelData.data.rideUUID)
                            }

                            NEW_RIDE_REQUEST -> {
                                val offerData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkActiveOfferResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkActiveOfferResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.ActiveOffer(
                                    offerData
                                )
                            }

                            OFFER_ACCEPTED -> {
                                Log.d("WebSocketLog", "@@@@ $jsonString")
                                val offerData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkActiveOfferResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkActiveOfferResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.OfferAccepted(
                                    offerData
                                )
                            }

                            else -> {
                                WebSocketEventNetwork.Unknown(jsonString)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocketLog", "Parsing error: ${e.message}")
                        WebSocketEventNetwork.Unknown(jsonString)
                    }
                }
            messageStates?.let {
                emitAll(messageStates)
            }
        }
    }

    override suspend fun sendLocation(data: NetworkSendLocationRequest) {
        session?.outgoing?.send(
            Frame.Text(Gson().toJson(data))
        )
        Log.d("WebSocketLog", "location sent")
    }

    override suspend fun close() {
        session?.close()
        session = null
        Log.d("WebSocketLog", "Session Closed")
    }

    companion object {
        const val NEW_RIDE_REQUEST = "new_ride_request"
        const val DRIVER_OFFER = "driver_offer"
        const val OFFER_ACCEPTED = "offer_accepted"
        const val OFFER_REJECTED = "offer_rejected"
        const val RIDE_STATUS_UPDATE = "ride_status_update"
        const val LOCATION_UPDATE = "location_update"
        const val RIDE_ACCEPTED = "ride_accepted"
        const val RIDE_CANCELED = "ride_cancel"
        const val RIDE_DELETED = "ride_deleted"
        const val RIDE_AMOUNT_UPDATED = "ride_amount_updated"
        const val ERROR = "error"
    }
}