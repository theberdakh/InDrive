package com.aralhub.indrive.core.data.model.cancel

data class CancelCause(
    val name: String,
    val type: String,
    val isActive: Boolean,
    val id: Int
)
