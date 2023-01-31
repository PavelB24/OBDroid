package ru.barinov.obdroid.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object ObdEventBus {

    private val _eventFlow = MutableSharedFlow<ObdEvents>()
    val eventFlow: SharedFlow<ObdEvents> = _eventFlow



    suspend fun onStateChanged(event: ObdEvents){
        _eventFlow.emit(event)
    }



    sealed class ObdEvents(){

        object SuccessConnect: ObdEvents()

        data class ConnectionFailed(val exception: Exception): ObdEvents()
    }
}