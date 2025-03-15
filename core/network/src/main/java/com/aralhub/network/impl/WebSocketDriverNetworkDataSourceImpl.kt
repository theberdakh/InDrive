package com.aralhub.network.impl

import android.util.Log
import com.aralhub.network.WebSocketDriverNetworkDataSource
import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.driver.NetworkActiveRideByDriverResponse
import com.aralhub.network.models.location.NetworkSendLocationRequest
import com.aralhub.network.models.offer.NetworkActiveOfferResponse
import com.aralhub.network.models.offer.NetworkOfferCancelResponse
import com.aralhub.network.models.offer.NetworkOfferRejectedResponse
import com.aralhub.network.utils.StartedRideWebSocketEventNetwork
import com.aralhub.network.utils.WebSocketEventNetwork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
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
    private var rideStatusSession: WebSocketSession? = null

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
                                val offerData =
                                    Gson().fromJson<WebSocketServerResponse<NetworkActiveRideByDriverResponse>>(
                                        jsonString,
                                        object :
                                            TypeToken<WebSocketServerResponse<NetworkActiveRideByDriverResponse>>() {}.type
                                    )
                                WebSocketEventNetwork.OfferAccepted(
                                    offerData.data
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

    //{"type":"offer_accepted",
    //"data":{"id":301,"uuid":"24ab31b3-6eba-47b6-8efd-5e4d375c6078","status":"agreed_with_driver","amount":7000.0,"wait_amount":0.0,"distance":2056.1,
    //"locations":{"points":[{"coordinates":{"longitude":59.61438666666666,"latitude":42.47357333333333},"name":"4R-191, 59"},
    //{"coordinates":{"longitude":59.60338592529297,"latitude":42.4633674621582},"name":"Nókis oraylıq diyxan bazarı"}]},
    //"is_active":true,"created_at":"2025-03-15T05:45:47.678916Z",
    //"passenger":{"id":40,"full_name":"Atabek Otebaev","phone_number":"+998913821929","rating":0.0,"photo_url":null},
    //"payment_method":{"id":2,"name":"string","is_active":true},"options":[],"is_commission_applied":false}}
    override suspend fun sendLocation(data: NetworkSendLocationRequest) {
        session?.outgoing?.send(
            Frame.Text(Gson().toJson(data))
        )
        Log.d("WebSocketLog", "location sent")
    }

    override suspend fun close() {
        session?.close(
            CloseReason(
                CloseReason.Codes.NORMAL, "Closing Session"
            )
        )
        session = null
        Log.d("WebSocketLog", "Session Closed")
    }

    override fun getStartedRideStatus(): Flow<StartedRideWebSocketEventNetwork> {
        return flow {
            rideStatusSession = client.webSocketSession {
                url("ws://araltaxi.aralhub.uz/ride/wb")
            }
            if (rideStatusSession?.isActive == true) {
                Log.d("WebSocketLog", "Started ride web socket Connected")
            }
            val messageStates = rideStatusSession
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
                            RIDE_CANCELED_BY_PASSENGER -> {
                                StartedRideWebSocketEventNetwork.RideCancelledByPassenger
                            }

                            else -> {
                                StartedRideWebSocketEventNetwork.UnknownAction(jsonString)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocketLog", "Parsing error: ${e.message}")
                        StartedRideWebSocketEventNetwork.UnknownAction(jsonString)
                    }
                }
            messageStates?.let {
                emitAll(messageStates)
            }
        }
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
        const val RIDE_CANCELED_BY_PASSENGER = "cancelled_by_passenger"
        const val RIDE_DELETED = "ride_deleted"
        const val RIDE_AMOUNT_UPDATED = "ride_amount_updated"
        const val ERROR = "error"
    }
}