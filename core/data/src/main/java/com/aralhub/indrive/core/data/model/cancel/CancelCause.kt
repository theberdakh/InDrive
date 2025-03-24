package com.aralhub.indrive.core.data.model.cancel

import com.aralhub.network.models.cancel.NetworkDriverCancelCause

data class CancelCause(
    val name: String,
    val type: String,
    val isActive: Boolean,
    val id: Int
)

data class DriverCancelCause(
    val id: Int,
    val name: String
)

fun List<NetworkDriverCancelCause>.toDomain() = this.map {
    DriverCancelCause(
        id = it.id,
        name = it.name
    )
}
