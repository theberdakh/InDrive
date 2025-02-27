package com.aralhub.network.models.card

import com.google.gson.annotations.SerializedName

data class NetworkCard(
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("name_on_card")
    val nameOnCard: String
)
