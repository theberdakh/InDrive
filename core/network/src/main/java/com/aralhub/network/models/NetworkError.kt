package com.aralhub.network.models

data class CustomError(
    val detail: CustomErrorDetail
)

data class CustomErrorDetail(
    val ru: String,
    val en: String,
    val kk: String
)

data class ValidationError(
   val detail: List<ValidationErrorDetail>
)

data class ValidationErrorDetail(
    val type: String,
    val loc: List<String>,
    val msg: String,
    val input: String,
    val ctx: ValidationErrorDetailContext
)

data class ValidationErrorDetailContext(
    val error: Map<String, Any>
)
