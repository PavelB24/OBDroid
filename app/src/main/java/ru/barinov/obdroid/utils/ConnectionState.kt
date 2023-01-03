package ru.barinov.obdroid.utils

import android.bluetooth.BluetoothSocket
import android.net.LinkProperties
import android.net.Network
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

sealed class ConnectionState {

    data class WifiConnected(val network: Network) : ConnectionState()

    object UnAvailable : ConnectionState()

    data class ConnectedToBt(
        val bt: BtConnectionItem,
        val socket: BluetoothSocket
    ) : ConnectionState()

    data class LinkPropertiesChanged(
        val network: Network,
        val linkProperties: LinkProperties
    ): ConnectionState()

    data class Lost(val network: Network): ConnectionState()

    object OnAddressConfirmed: ConnectionState()

}
