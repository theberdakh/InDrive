package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.models.offer.NetworkActiveOfferResponse
import com.aralhub.network.models.offer.NetworkOfferCancelResponse
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
                        val baseResponse =
                            Gson().fromJson(jsonString, WebSocketServerResponse::class.java)

                        when (baseResponse.type) {
                            "ride_cancel" -> {
                                val rideCancelData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkOfferCancelResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkOfferCancelResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.RideCancel(rideCancelData.data.rideId)
                            }

                            "new_ride_request" -> {
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
    }
}