package com.aralhub.network.models.ride

import com.aralhub.network.models.driver.NetworkDriverActive
import com.aralhub.network.models.location.NetworkLocationPoint
import com.aralhub.network.models.location.NetworkLocations
import com.aralhub.network.models.option.NetworkOption
import com.aralhub.network.models.payment.NetworkPaymentMethod
import com.google.gson.annotations.SerializedName

/*
* {"id":208,
* "uuid":"02f4df77-42bc-4025-8c74-c970d94086b1",
* "status":"agreed_with_driver",
* "amount":7000.0,
* "wait_amount":0.0,
* "distance":0.0,
* "locations":{
* "points":[
* {"coordinates":{"longitude":59.609103,"latitude":42.464292},"name":"Xaliqlar Doslig'i mahalla fuqarolar yig'ini"},
* {"coordinates":{"longitude":59.609103,"latitude":42.464292},"name":"Xaliqlar Doslig'i mahalla fuqarolar yig'ini"}]},
* "is_active":true,
* "created_at":"2025-03-10T16:40:54.505727Z",
* "driver":{"driver_id":6,"full_name":"testov test","rating":5.0,"photo_url":"uploads/driver_photo_6.webp","color":{"ru":"string","en":"string","kk":"string"},"vehicle_type":{"ru":"string","en":"string","kk":"string"},"plate_number":"123fdas","phone_number":"+998912345678"},
* "payment_method":{"id":2,"name":"string","is_active":true},
* "options":[],
* "is_commission_applied":false}}*/
data class NetworkRideActive(
    val id: Int,
    val uuid: String,
    val status: String,
    val amount: Int,
    @SerializedName("wait_amount")
    val waitAmount: Double,
    val distance: Double,
    val locations: NetworkLocations,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    val driver: NetworkDriverActive,
    @SerializedName("payment_method")
    val paymentMethod: NetworkPaymentMethod,
    val options: List<NetworkOption>
)