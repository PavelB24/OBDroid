package ru.barinov.obdroid

import android.bluetooth.BluetoothSocket
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

class ConnectionWatcher {

    private val _connectionState =
        MutableStateFlow<ConnectionState>(ConnectionState.UnAvailable)

    val connectionState: StateFlow<ConnectionState> = _connectionState

    var currWifiConnectionSsid: String? = null
        private set
    var currWifiConnectionBssid: String? = null
        private set


    fun onChangeNetworkState(state: ConnectionState) {
        _connectionState.value = state
    }

    fun getCurrentWfConnection(): Network? {
        return when (connectionState.value) {
            is ConnectionState.Connected -> {
                (connectionState.value as ConnectionState.Connected).network
            }
            is ConnectionState.LinkPropertiesChanged -> {
                (connectionState.value as ConnectionState.LinkPropertiesChanged).network
            }
            else -> null
        }
    }

    fun getCurrentBtConnection(): BluetoothSocket?{
        return if(connectionState.value is ConnectionState.ConnectedToBt){
            (connectionState.value as ConnectionState.ConnectedToBt).socket
        } else null
    }

    fun getCurrentBtDevice(): ConnectionState? {
        return if(connectionState.value is ConnectionState.ConnectedToBt){
            connectionState.value
        } else null
    }

    fun setCurrentSignatures(bssid: String, ssid: String){
        currWifiConnectionBssid = bssid
        currWifiConnectionSsid = ssid
    }




}