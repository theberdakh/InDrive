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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
            val clientWebSocketEvent = webSocketSession.incoming.consumeAsFlow().filterIsInstance<Frame.Text>()
                    .map { frameText ->
                        Log.i("WebSocketLog", "Frame: ${frameText.readText()}")
                        try {
                            val data = Gson().fromJson(frameText.readText(),
                                ClientWebSocketServerResponse::class.java
                            )
                            when (data.type) {
                                DRIVER_OFFER -> {
                                    val driverOffer =
                                        Gson().fromJson<ClientWebSocketServerResponse<NetworkOffer>>(
                                            frameText.readText(),
                                            object :
                                                TypeToken<ClientWebSocketServerResponse<NetworkOffer>>() {}.type
                                        )
                                    ClientWebSocketEventNetwork.DriverOffer(driverOffer.data)
                                }

                                else -> ClientWebSocketEventNetwork.Unknown("Unknown")
                            }
                        } catch (e: Exception) {
                            Log.e("WebSocketLog", "Error: ${e.message}")
                            ClientWebSocketEventNetwork.Unknown("Unknown")
                        }
                    }
            flow { emitAll(clientWebSocketEvent) }
        } ?: flow {
            Log.i("WebSocketLog", "Session is null")
            emit(ClientWebSocketEventNetwork.Unknown("Unknown")) }
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

    companion object {
        private const val DRIVER_OFFER = "driver_offer"
    }
}