package ru.barinov.obdroid.utils

import android.bluetooth.BluetoothSocket
import android.net.Network
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConnectionWatcher {

    private val _connectionState =
        MutableSharedFlow<ConnectionState>()

    val connectionState: SharedFlow<ConnectionState> = _connectionState

    var lastState: ConnectionState = ConnectionState.UnAvailable

    var lastWifiConnectionSsid: String? = null
        private set
    var lastWifiConnectionBssid: String? = null
        private set



    fun onChangeState(state: ConnectionState) {
        Log.d("@@@", "STATE CHANGED TO $state")
        if(state is ConnectionState.Lost){
            lastWifiConnectionSsid = null
            lastWifiConnectionBssid = null
        }
        CoroutineScope(Dispatchers.IO).launch {
            lastState = state
            _connectionState.emit(state)
        }
    }

    fun getCurrentWfConnection(): ConnectionState? {
        return when (lastState) {
            is ConnectionState.WifiConnected -> {
                lastState
            }
            is ConnectionState.LinkPropertiesChanged -> {
                lastState
            }
            else -> null
        }
    }


    fun getCurrentBtDevice(): ConnectionState.BtSocketObtained? {
        return if(lastState is ConnectionState.BtSocketObtained){
            lastState as ConnectionState.BtSocketObtained
        } else null
    }

    fun setCurrentSignatures(bssid: String, ssid: String){
        lastWifiConnectionBssid = bssid
        lastWifiConnectionSsid = ssid
    }




}