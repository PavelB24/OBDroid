package ru.barinov.obdroid.core

import android.app.ActivityManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object ObdBus {

    private val _eventFlow = MutableSharedFlow<ObdEvents>()
    val eventFlow: SharedFlow<ObdEvents> = _eventFlow

    var lastEvent: ObdEvents? = null



    suspend fun onStateChanged(event: ObdEvents){
        lastEvent = event
        _eventFlow.emit(event)
    }

    fun checkForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo().also {
            ActivityManager.getMyMemoryState(it)
        }
        return appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE
    }



    sealed class ObdEvents(){

        object SuccessConnect: ObdEvents()

        data class ConnectionFailed(val exception: Exception? = null): ObdEvents()
    }

}