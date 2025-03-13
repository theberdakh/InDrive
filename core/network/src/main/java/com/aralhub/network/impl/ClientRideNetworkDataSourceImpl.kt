package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.ClientRideNetworkDataSource
import com.aralhub.network.models.ClientWebSocketServerResponse
import com.aralhub.network.models.ride.NetworkRideStatus
import com.aralhub.network.utils.ClientWebSocketEventRide
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

class ClientRideNetworkDataSourceImpl(private val client: HttpClient) :
    ClientRideNetworkDataSource {
    private var session: WebSocketSession? = null

    override suspend fun getRide(): Flow<ClientWebSocketEventRide> {
        session = client.webSocketSession { url("ws://araltaxi.aralhub.uz/ride/wb") }

        return session?.let { webSocketSession ->
            Log.i("WebSocketLog", "Session is not null")
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
                                        RIDE_STATUS_UPDATE -> {
                                            val driverOffer =
                                                Gson().fromJson<ClientWebSocketServerResponse<NetworkRideStatus>>(
                                                    text,
                                                    object :
                                                        TypeToken<ClientWebSocketServerResponse<NetworkRideStatus>>() {}.type
                                                )
                                            ClientWebSocketEventRide.RideUpdate(driverOffer.data)
                                        }

                                        else -> ClientWebSocketEventRide.Unknown("Unknown type: ${data.type}")
                                    }

                                    emit(event)
                                } catch (e: Exception) {
                                    Log.e("WebSocketLog", "Error parsing frame: ${e.message}", e)
                                    emit(ClientWebSocketEventRide.Unknown("Parsing error: ${e.message}"))
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("WebSocketLog", "WebSocket error: ${e.message}", e)
                            emit(ClientWebSocketEventRide.Unknown("WebSocket error: ${e.message}"))
                        }
                    }
                }
        } ?: flow {
            Log.i("WebSocketLog", "Session is null")
            emit(ClientWebSocketEventRide.Unknown("Session initialization failed"))
        }
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

    companion object {
        private const val RIDE_STATUS_UPDATE = "ride_status_update"

    }
}