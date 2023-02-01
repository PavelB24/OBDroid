package ru.barinov.obdroid.utils

import android.bluetooth.BluetoothSocket
import android.net.Network
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConnectionWatcher {

    private val _connectionState =
        MutableStateFlow<ConnectionState>(ConnectionState.UnAvailable)

    val connectionState: StateFlow<ConnectionState> = _connectionState

    var lastWifiConnectionSsid: String? = null
        private set
    var lastWifiConnectionBssid: String? = null
        private set



    fun onChangeState(state: ConnectionState) {
        Log.d("@@@", "STATE CHANGED TO $state")
        _connectionState.value = state
    }

    fun getCurrentWfConnection(): ConnectionState? {
        return when (connectionState.value) {
            is ConnectionState.WifiConnected -> {
                connectionState.value
            }
            is ConnectionState.LinkPropertiesChanged -> {
                connectionState.value
            }
            else -> null
        }
    }


    fun getCurrentBtDevice(): ConnectionState.BtSocketObtained? {
        return if(connectionState.value is ConnectionState.BtSocketObtained){
            connectionState.value as ConnectionState.BtSocketObtained
        } else null
    }

    fun setCurrentSignatures(bssid: String, ssid: String){
        lastWifiConnectionBssid = bssid
        lastWifiConnectionSsid = ssid
    }




}