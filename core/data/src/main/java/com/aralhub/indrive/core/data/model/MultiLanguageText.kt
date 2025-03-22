package com.aralhub.indrive.core.data.model

import com.aralhub.network.models.NetworkMultiLanguageText

fun NetworkMultiLanguageText.toDomain() = MultiLanguageText(
    ru = ru,
    en = en,
    kk = kk
)

data class MultiLanguageText(
    val ru: String,
    val en: String,
    val kk: String
)
