package ru.barinov.obdroid.ui.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.barinov.obdroid.ConnectedEventType
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.core.toBtConnectionItem
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem
import java.util.*

class ConnectionsViewModel(
    private val connectionHandler: ConnectionActionHandler
) : ViewModel() {

    private val listHandler by lazy { ConnectionsListHandler(viewModelScope) }

    private val _onConnectFlow = connectionHandler.onConnectFlow
    val onConnectFlow: SharedFlow<ConnectedEventType> = _onConnectFlow

    val scanResult: SharedFlow<List<ConnectionItem>> = listHandler.scanResult


    fun onConnectBt(socket: BluetoothSocket, actions: BtConnectionI) {

    }


    fun getConnectionHandler() = connectionHandler

    @SuppressLint("MissingPermission")
    fun handleBtDevice(device: BluetoothDevice, andConnect: Boolean = false) {
        val handledBt = device.toBtConnectionItem(object :
            BtConnectionI {

            override fun createBound(): Boolean {
                return device.createBond()
            }

            override fun connect(): BluetoothSocket {
                val uuid = UUID.fromString(BtConnectionItem.BT_UUID)
                return device.createInsecureRfcommSocketToServiceRecord(uuid)
            }
        })
        listHandler.addBt(handledBt)
        if(andConnect){
            connectionHandler.connectBt(handledBt)
        }
    }

    fun handleScanResults(scanResult: List<ScanResult>) {
        val result = scanResult.map {
            WifiConnectionItem(
                ConnectionItem.ConnectionType.WIFI,
                it.BSSID,
                it.frequency,
                it.timestamp,
                it.channelWidth,
                it.SSID
            )
        }
        listHandler.addWiFi(result)
    }

}