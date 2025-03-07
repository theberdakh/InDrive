package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.ClientOffersNetworkDataSource
import com.aralhub.network.models.ClientWebSocketServerResponse
import com.aralhub.network.models.offer.NetworkOffer
import com.aralhub.network.utils.ClientWebSocketEventNetwork
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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

class ClientOffersNetworkDataSourceImpl(private val client: HttpClient) :
    ClientOffersNetworkDataSource {
    private var session: WebSocketSession? = null

    override suspend fun getOffers(): Flow<ClientWebSocketEventNetwork> {
        Log.i("WebSocketLog", "gettingOffers")
        session = client.webSocketSession { url("ws://araltaxi.aralhub.uz/websocket/wb/passenger") }
        Log.i("WebSocketLog", "Session: ${session?.isActive}")

        return session?.let { webSocketSession ->
            Log.i("WebSocketLog", "Session is not null")

            // Add debugging to confirm the flow is being processed
            webSocketSession.incoming
                .consumeAsFlow()
                .onEach { frame ->
                    Log.i("WebSocketLog", "Received a frame of type: ${frame::class.java.simpleName}")
                }
                .filterIsInstance<Frame.Text>()
                .onEach { frame ->
                    Log.i("WebSocketLog", "Processing Text frame")
                }
                .let { textFramesFlow ->
                    flow {
                        try {
                            textFramesFlow.collect { frameText ->
                                val text = frameText.readText()
                                Log.i("WebSocketLog", "Received raw frame: $text")

                                // Rest of your processing logic
                                try {
                                    val data = Gson().fromJson(text, ClientWebSocketServerResponse::class.java)
                                    Log.i("WebSocketLog", "Parsed type: ${data.type}")

                                    val event = when (data.type) {
                                        DRIVER_OFFER -> {
                                            val driverOffer = Gson().fromJson<ClientWebSocketServerResponse<NetworkOffer>>(
                                                text,
                                                object : TypeToken<ClientWebSocketServerResponse<NetworkOffer>>() {}.type
                                            )
                                            ClientWebSocketEventNetwork.DriverOffer(driverOffer.data)
                                        }
                                        else -> ClientWebSocketEventNetwork.Unknown("Unknown type: ${data.type}")
                                    }

                                    emit(event)
                                } catch (e: Exception) {
                                    Log.e("WebSocketLog", "Error parsing frame: ${e.message}", e)
                                    emit(ClientWebSocketEventNetwork.Unknown("Parsing error: ${e.message}"))
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("WebSocketLog", "WebSocket error: ${e.message}", e)
                            emit(ClientWebSocketEventNetwork.Unknown("WebSocket error: ${e.message}"))
                        }
                    }
                }
        } ?: flow {
            Log.i("WebSocketLog", "Session is null")
            emit(ClientWebSocketEventNetwork.Unknown("Session initialization failed"))
        }
    }


    override suspend fun close() {
        session?.close()
        session = null
    }

    companion object {
        private const val DRIVER_OFFER = "driver_offer"
    }
}