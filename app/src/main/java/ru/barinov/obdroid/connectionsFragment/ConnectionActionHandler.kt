package ru.barinov.obdroid.connectionsFragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.net.ConnectivityManager
import android.net.MacAddress
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.barinov.obdroid.ConnectedEventType
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

class ConnectionActionHandler(
    private val context: Context,
) : ConnectionsAdapter.ConnectionClickListener {


    val connectFlow: MutableSharedFlow<ConnectedEventType> = MutableSharedFlow(0, 10)


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
                                if (!item.ssid.isNullOrEmpty()) {
                                    builder.setSsid(item.ssid)
                                }
                                val networkRequest = NetworkRequest.Builder()
                                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                    .setNetworkSpecifier(builder.build())
                                    .build()
                                cm.requestNetwork(
                                    networkRequest,
                                    WifiConnectionCallBack() { network ->
                                        val result = cm.bindProcessToNetwork(network)
                                        if (result) {
//                                          cm.getLinkProperties(network)?.routes?.last()?.gateway?.hostAddress
                                            onConnection(
                                                ConnectedEventType.WifiConnected(
                                                    network,
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

    fun connectBt(item: BtConnectionItem) {
        val socket = item.actions.connect()
        onConnection(
            if (socket != null)
                ConnectedEventType.BluetoothConnected(
                    socket,
                    item
                )
            else ConnectedEventType.Fail
        )
    }

    private fun onConnection(event: ConnectedEventType) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            connectFlow.emit(event)
        }
    }


}
