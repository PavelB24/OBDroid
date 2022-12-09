package ru.barinov.obdroid

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiConnectionWatcher {

    private val _connectionState = MutableStateFlow<WifiConnectionState>(WifiConnectionState.UnAvailable)

    val connectionState : StateFlow<WifiConnectionState> = _connectionState


    fun onChangeNetworkState(state : WifiConnectionState){
        _connectionState.value = state
    }



}