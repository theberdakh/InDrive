package com.aralhub.indrive.core.data.util

import kotlinx.coroutines.flow.MutableSharedFlow

var webSocketEvent = MutableSharedFlow<WebSocketEvent>(replay = 1)

var closeActiveOrdersWebSocket = MutableSharedFlow<Unit>(replay = 1)