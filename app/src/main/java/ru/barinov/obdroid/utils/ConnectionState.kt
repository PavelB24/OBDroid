package ru.barinov.obdroid.utils

import android.bluetooth.BluetoothSocket
import android.net.LinkProperties
import android.net.Network

sealed class ConnectionState {

    data class WifiConnected(
        val bssid: String,
        val network: Network) : ConnectionState()

    object UnAvailable : ConnectionState()

    data class BtSocketObtained(
        val mac: String,
        val socket: BluetoothSocket?
    ) : ConnectionState()

    data class LinkPropertiesChanged(
        val bssid: String,
        val network: Network,
        val linkProperties: LinkProperties
    ): ConnectionState()

    data class Lost(val network: Network): ConnectionState()

    data class OnAddressConfirmed(
        val bssid: String,
        val network: Network): ConnectionState()

}
