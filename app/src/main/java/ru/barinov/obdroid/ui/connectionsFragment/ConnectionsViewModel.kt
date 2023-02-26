package ru.barinov.obdroid.ui.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.barinov.obdroid.domain.ConnectedEventType
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.core.ObdBus
import ru.barinov.obdroid.core.toBtConnectionItem
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.BtItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem
import ru.barinov.obdroid.utils.ConnectionState
import java.util.*

class ConnectionsViewModel(
    private val connectionHandler: ConnectionHandler,
) : ViewModel() {

    val eventBusFlow = ObdBus.eventFlow

    private val listHandler by lazy { ConnectionsListHandler(viewModelScope) }

    private val _onConnectFlow = connectionHandler.onConnectFlow

    val onConnectFlow: SharedFlow<ConnectedEventType> = _onConnectFlow

    val scanResult: SharedFlow<List<ConnectionItem>> = listHandler.scanResult

    fun getConnectionHandler() = connectionHandler

    @SuppressLint("MissingPermission")
    fun handleBtDevice(
        device: BluetoothDevice,
        rssi: Short?,
        clazz: BluetoothClass?,
        andConnect: Boolean = false
    ) {
        val currentConnection = connectionHandler.getCurrentBtConnection()
        if (currentConnection == null || (currentConnection.mac != device.address
            && ObdBus.lastEvent is ObdBus.ObdEvents.SuccessConnect)
        ) {
            val handledBt = device.toBtConnectionItem(
                rssi,
                clazz,
                object: BtConnectionI {

                    override fun createBound(): Boolean {
                        return device.createBond()
                    }

                    override fun connect(): BluetoothSocket {
                        val uuid = UUID.fromString(BtConnectionItem.BT_UUID)
                        //device.uuids[0].uuid
                        //UUID.fromString("0000111f-0000-1000-8000-00805f9b34fb")

                        return device.createInsecureRfcommSocketToServiceRecord(uuid)
                    }
                })

            listHandler.addBt(handledBt)
            if (andConnect) {
                connectionHandler.connectBt(handledBt)
            }
        }
    }


    fun connectBtDirectly(
        address: String,
        socket: BluetoothSocket
    ){
        connectionHandler.connectBt(address, socket)
    }

    fun handleScanResults(scanResult: List<ScanResult>) {
        val currentConnection = connectionHandler.getCurrentWifiConnection()
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
        currentConnection?.let { state ->
            when (state) {
                is ConnectionState.WifiConnected -> {
                    result.filter { it.bssid != state.bssid }
                }
                is ConnectionState.OnAddressConfirmed -> {
                    result.filter { it.bssid != state.bssid }
                }
                else -> throw IllegalStateException()
            }
        }
        listHandler.addWiFi(result)
    }

    fun removeConnectedWiFi(item: WifiConnectionItem) {
        listHandler.removeItem(item)
    }

    fun removeConnectedBt(item: BtItem) {
        listHandler.removeItem(item)
    }

}