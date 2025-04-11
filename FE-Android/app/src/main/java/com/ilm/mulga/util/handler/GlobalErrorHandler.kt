package com.ilm.mulga.util.handler

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object GlobalErrorHandler {
    // replay 값을 1로 설정하여 마지막 이벤트를 보존
    private val _errorEvents = MutableSharedFlow<String>(replay = 1)
    val errorEvents = _errorEvents.asSharedFlow()

    fun emitError(error: String) {
        if (error.contains("USER")) return
        _errorEvents.tryEmit(error)
    }
}
