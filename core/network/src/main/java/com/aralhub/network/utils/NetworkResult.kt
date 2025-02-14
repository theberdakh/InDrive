package com.aralhub.network.utils

/**
 * Error model of server's response error to display error message to user in UI (now only 422 error send this model, otherwise just a NetworkMessage)
 *
 * */
//@Serializable
data class NetworkServerError(
    val message: String,
    val errors: Map<String, List<String>>,
)

//@Serializable
data class NetworkServerMessage(
    val message: String,
)