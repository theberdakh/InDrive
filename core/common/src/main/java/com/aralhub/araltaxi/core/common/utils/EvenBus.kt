package com.aralhub.araltaxi.core.common.utils

import kotlinx.coroutines.flow.MutableSharedFlow

var rejectOfferState = MutableSharedFlow<Unit>(replay = 0)