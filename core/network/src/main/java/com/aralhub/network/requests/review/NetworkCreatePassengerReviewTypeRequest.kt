package com.aralhub.network.requests.review

import com.google.gson.annotations.SerializedName

data class NetworkCreatePassengerReviewTypeRequest(
    @SerializedName("name_ru")
    val nameRu: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("name_kk")
    val nameKk: String
)