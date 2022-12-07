package ru.barinov.obdroid.connectionsFragment

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.barinov.obdroid.ConnectedEventType
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

class ConnectionsViewModel(
    private val connectionHandler: ConnectionActionHandler
) : ViewModel() {

    private val listHandler by lazy { ConnectionsListHandler() }

    private val _onConnectFlow = connectionHandler.connectFlow
    val onConnectFlow: SharedFlow<ConnectedEventType> = _onConnectFlow

    fun onConnectWf(
        network : Network,
        ssid : String,
        bssid : String
    ) {

    }

    fun onConnectBt(socket : BluetoothSocket, actions : BtConnectionI) {

    }


    fun connectBounded(device : BtConnectionItem){
        connectionHandler.connectBt(device)
    }

    fun handle(btDevice : BtConnectionItem) = listHandler.addBt(btDevice)

    fun handle(wifiList : List<WifiConnectionItem>) = listHandler.addWiFi(wifiList)


    fun getConnectionHandler() = connectionHandler

}