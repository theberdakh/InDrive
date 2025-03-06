package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.ClientOffersNetworkDataSource
import com.aralhub.network.models.ClientWebSocketServerResponse
import com.aralhub.network.models.offer.NetworkOffer
import com.aralhub.network.utils.ClientWebSocketEvent
import com.aralhub.network.utils.WebSocketEventNetwork
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.isActive

class ClientOffersNetworkDataSourceImpl(private val client: HttpClient): ClientOffersNetworkDataSource {
    private var session: WebSocketSession? = null

    override fun getOffers(): Flow<ClientWebSocketEvent> {
        return flow {
            session = client.webSocketSession {
                url("ws://araltaxi.aralhub.uz/websocket/wb/passenger")
            }
            if (session?.isActive == true) {
                Log.d("WebSocketLog", "Connected")
            }
            val messageStates= session
                ?.incoming
                ?.consumeAsFlow()
                ?.filterIsInstance<Frame.Text>()
                ?.mapNotNull { frame ->
                    val jsonString = frame.readText()
                    Log.d("WebSocketLog", jsonString)
                    try {
                        val response = Gson().fromJson(jsonString, ClientWebSocketServerResponse::class.java)
                        when (response.type) {
                            DRIVER_OFFER -> {
                                val offer = Gson().fromJson<ClientWebSocketServerResponse<NetworkOffer>>(jsonString, NetworkOffer::class.java)
                                ClientWebSocketEvent.DriverOffer(offer)
                            }
                            else -> {
                                ClientWebSocketEvent.Unknown(jsonString)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocketLog", "Parsing error: ${e.message}")
                        ClientWebSocketEvent.Unknown(jsonString)
                    }
                }
            messageStates?.let {
                emitAll(messageStates)
            }
        }
    }

    companion object {
        private const val DRIVER_OFFER = "driver_offer"
    }
}