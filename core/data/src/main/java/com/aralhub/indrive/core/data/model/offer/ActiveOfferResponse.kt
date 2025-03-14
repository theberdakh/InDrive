package com.aralhub.indrive.core.data.model.offer

import com.aralhub.network.models.WebSocketServerResponse
import com.aralhub.network.models.driver.NetworkActiveRideByDriverResponse
import com.aralhub.network.models.offer.NetworkActiveOfferResponse

data class ActiveOfferResponse(
    val id: Int,
    val uuid: String,
    val name: String,
    val pickUp: String?,
    val avatar: String,
    val roadPrice: String = "",
    val pickUpDistance: Double,
    val roadDistance: Double,
    val paymentType: Int,
    val pickUpAddress: String?,
    val destinationAddress: String? = null,
    val locations: List<ClientRideLocationsItems>
)

data class ClientRideLocationsItems(
    val coordinates: ClientRideLocationsItemsCoordinates,
    val name: String
)

data class ClientRideLocationsItemsCoordinates(
    val longitude: Number,
    val latitude: Number
)

fun WebSocketServerResponse<NetworkActiveOfferResponse>.toDomain(): ActiveOfferResponse =
    with(this) {
        return ActiveOfferResponse(
            id = 1,
            uuid = data.uuid,
            name = data.passenger.userFullName,
            pickUp = data.clientPickUpAddress,
            avatar = data.passenger.avatar ?: "https://randomuser.me/api/portraits/men/8.jpg",
            roadPrice = data.baseAmount.toString(),
            pickUpDistance = distanceToClient.distance,
            roadDistance = data.distance.totalDistance.toDouble(),
            paymentType = data.paymentMethod.id,
            pickUpAddress = data.clientPickUpAddress,
            destinationAddress = data.locations.points.getOrNull(1)?.name,
            locations = data.locations.points.map {
                ClientRideLocationsItems(
                    ClientRideLocationsItemsCoordinates(
                        it.coordinates.longitude,
                        it.coordinates.latitude
                    ),
                    it.name
                )
            }
        )
    }

fun NetworkActiveRideByDriverResponse.toActiveOfferDomain(): ActiveOfferResponse =
    with(this) {
        return ActiveOfferResponse(
            id = id,
            uuid = uuid,
            name = passenger.userFullName,
            pickUp = locations.points.getOrNull(0)?.name,
            avatar = passenger.avatar ?: "https://randomuser.me/api/portraits/men/9.jpg",
            roadPrice = amount.toString(),
            pickUpDistance = distance.toDouble(),
            roadDistance = distance.toDouble(),
            paymentType = paymentMethod.id,
            pickUpAddress = locations.points.getOrNull(1)?.name,
            destinationAddress = locations.points.getOrNull(1)?.name,
            locations = locations.points.map {
                ClientRideLocationsItems(
                    ClientRideLocationsItemsCoordinates(
                        it.coordinates.longitude,
                        it.coordinates.latitude
                    ),
                    it.name
                )
            }
        )
    }