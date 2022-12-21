package ru.barinov.obdroid.ui.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.NetworkSpecifier
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

    private val _onConnectFlow = connectionHandler.connectFlow
    val onConnectFlow: SharedFlow<ConnectedEventType> = _onConnectFlow

    val scanResult = listHandler.scanResult


    fun onConnectBt(socket: BluetoothSocket, actions: BtConnectionI) {

    }

    @SuppressLint("MissingPermission")
    fun connectBounded(device: BluetoothDevice) {
        connectionHandler.connectBt(
            device.toBtConnectionItem(object : BtConnectionI {
                override fun createBound(): Boolean {
                    return device.createBond()
                }

                override fun connect(): BluetoothSocket? {
                    val uuid = UUID.fromString(BtConnectionItem.BT_UUID)
                    return device.createInsecureRfcommSocketToServiceRecord(uuid)
                }
            }
            )
        )
    }


    fun getConnectionHandler() = connectionHandler

    fun handleBtDevice(device: BluetoothDevice) {
        val handledBt = device.toBtConnectionItem(object :
            BtConnectionI {
            @SuppressLint("MissingPermission")
            override fun createBound(): Boolean {
                return device.createBond()
            }

            @SuppressLint("MissingPermission")
            override fun connect(): BluetoothSocket {
                val uuid = UUID.fromString(BtConnectionItem.BT_UUID)
                return device.createInsecureRfcommSocketToServiceRecord(uuid)
            }
        })
        listHandler.addBt(handledBt)
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