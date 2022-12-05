package ru.barinov.obdroid.connectionsFragment

import android.bluetooth.BluetoothDevice
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
import android.widget.Toast
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

class ConnectionActionHandler(
    private val context: Context,
    private val connectFlow: MutableSharedFlow<ConnectedEventType>
) : ConnectionsAdapter.ConnectionClickListener {
    override fun onItemClick(item: ConnectionItem, itemView: View) {
        val popup = PopupMenu(context, itemView)
        popup.apply {
            when (item) {
                is BtConnectionItem -> {
                    inflate(R.menu.bt_item_popup_menu)
                    if (item.boundState != BluetoothDevice.BOND_BONDED) {
                        popup.menu.getItem(1).isVisible = false
                        setOnMenuItemClickListener {
                            when (it.itemId) {
                                R.id.bound_menuItem -> {
                                    item.actions.createBound()
                                }
                                R.id.connect_bt_item -> {
                                    if (item.boundState != BluetoothDevice.BOND_BONDED) {
                                        Toast.makeText(
                                            context,
                                            "Bound Device First",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        item.actions.connect()
                                    }
                                }
                            }
                            true
                        }
                    }
                }
                is WifiConnectionItem -> {
                    popup.apply {
                        inflate(R.menu.wifi_item_popup_menu)
                        setOnMenuItemClickListener {
                            val cm = context.getSystemService(ConnectivityManager::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val wfSpec = WifiNetworkSpecifier.Builder()
                                    .setSsid(item.ssid)
                                    .setBssid(MacAddress.fromString(item.bssid))
                                    .build()
                                val networkRequest = NetworkRequest.Builder()
                                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                    .setNetworkSpecifier(wfSpec)
                                    .build()
                                cm.requestNetwork(networkRequest, WifiConnectionCallBack(cm) {
                                    onConnection()
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

    private fun onConnection() {
        TODO("Not yet implemented")
    }

    enum class ConnectedEventType {
        WIFI, BLUETOOTH, BLUETOOTH_BOUND, FAIL
    }
}
