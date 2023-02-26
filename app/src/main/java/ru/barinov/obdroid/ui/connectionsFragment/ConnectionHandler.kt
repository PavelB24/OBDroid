package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.net.ConnectivityManager
import android.net.MacAddress
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.barinov.obdroid.domain.ConnectedEventType
import ru.barinov.obdroid.R
import ru.barinov.obdroid.utils.ConnectionWatcher
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.domain.BtConnector
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem
import ru.barinov.obdroid.utils.ConnectionState

class ConnectionHandler(
    private val context: Context,
    private val connectionWatcher : ConnectionWatcher,
    private val preferences: Preferences
) : ConnectionsAdapter.ConnectionClickListener, BtConnector {


    val onConnectFlow: MutableSharedFlow<ConnectedEventType> = MutableSharedFlow(
        0,
        10
    )


    fun getCurrentBtConnection() = connectionWatcher.getCurrentBtDevice()

    fun getCurrentWifiConnection() = connectionWatcher.getCurrentWfConnection()


    override fun onItemClick(item: ConnectionItem, itemView: View) {
        val popup = PopupMenu(context, itemView)
        popup.apply {
            when (item) {
                is BtConnectionItem -> {
                    inflate(R.menu.bt_item_popup_menu)
                    popup.menu.apply {
                        getItem(1).isVisible =
                            item.boundState == BluetoothDevice.BOND_BONDED
                        getItem(0).isVisible =
                            item.boundState != BluetoothDevice.BOND_BONDED
                    }
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.bound_menuItem -> {
                                item.actions.createBound()
                            }
                            R.id.connect_bt_item -> {
                                connectBt(item)
                            }
                        }
                        true
                    }
                }
                is WifiConnectionItem -> {
                    popup.apply {
                        inflate(R.menu.wifi_item_popup_menu)
                        setOnMenuItemClickListener {
                            val cm = context.getSystemService(ConnectivityManager::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val builder = WifiNetworkSpecifier.Builder()
                                    .setBssid(MacAddress.fromString(item.bssid))
                                    .setWpa2Passphrase("111111Ss")
                                if (item.ssid.isNotEmpty()) {
                                    builder.setSsid(item.ssid)
                                }
                                val networkRequest = NetworkRequest.Builder()
                                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                    .setNetworkSpecifier(builder.build())
                                    .build()
                                cm.requestNetwork(
                                    networkRequest,
                                    WifiConnectionCallBack(
                                        connectionWatcher = connectionWatcher,
                                        bssid = item.bssid,
                                        connManager = cm
                                    ) { result, network ->
                                        if (result) {
                                            cm.getLinkProperties(network)
                                                ?.routes
                                                ?.last()
                                                ?.gateway
                                                ?.hostAddress?.let {
                                                    preferences.wifiAddress = it
                                                }
                                            connectionWatcher.setCurrentSignatures(item.bssid,
                                                item.ssid
                                            )
                                            onConnection(
                                                ConnectedEventType.WifiConnected(
                                                    item
                                                )
                                            )
                                        }
                                    })


                            } else {
                                TODO("VERSION.SDK_INT < Q")
                            }
                            true
                        }
                    }
                }
            }
            gravity = Gravity.END
            show()
        }
    }

    override fun connectBt(item: BtConnectionItem) {
        try {
            val socket = item.actions.connect()
            onConnection(
                if (socket != null)
                    ConnectedEventType.BluetoothConnecting(
                        item.toItemWithExtractedSocket()
                    )
                else ConnectedEventType.Fail
            )
            socket?.let {
                connectionWatcher.onChangeState(
                    ConnectionState.BtSocketObtained(
                        item.address,
                        socket
                    )
                )
            }
        } catch (e: Exception){
            e.printStackTrace()
            onConnection(
                ConnectedEventType.Fail
            )
        }
    }

    private fun onConnection(event: ConnectedEventType) {
        CoroutineScope(Dispatchers.IO).launch {
            onConnectFlow.emit(event)
        }
    }

    override fun connectBt(
        address: String,
        socket: BluetoothSocket
    ) {
        connectionWatcher.onChangeState(ConnectionState.BtSocketObtained(address, socket))
    }


}
