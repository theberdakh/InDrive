package com.aralhub.ui.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object LifecycleOwnerEx {

    fun <T> LifecycleOwner.observeState(stateFlow: SharedFlow<T>, action: (T) -> Unit) {
        stateFlow.onEach(action).launchIn(lifecycleScope)
    }
}