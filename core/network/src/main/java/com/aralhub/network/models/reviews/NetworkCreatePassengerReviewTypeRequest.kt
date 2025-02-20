package com.aralhub.network.models.reviews

import com.google.gson.annotations.SerializedName

data class NetworkCreatePassengerReviewTypeRequest(
    @SerializedName("name_ru")
    val nameRu: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("name_kk")
    val nameKk: String
)

data class NetworkCreatePassengerReviewTypeResponse(
    val name: String,
    val id: Int
)