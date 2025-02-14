package com.aralhub.data.util

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    fun isOnline(): Flow<Boolean>
}