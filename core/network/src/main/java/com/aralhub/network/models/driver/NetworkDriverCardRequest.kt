package com.aralhub.network.models.driver

import com.google.gson.annotations.SerializedName

data class NetworkDriverCardRequest(
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("name_on_card")
    val nameOnCard: String
)

data class NetworkDriverCardResponse(
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("name_on_card")
    val nameOnCard: String
)

