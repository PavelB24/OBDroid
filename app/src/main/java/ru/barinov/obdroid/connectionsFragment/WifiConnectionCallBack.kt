package ru.barinov.obdroid.connectionsFragment

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Build

class WifiConnectionCallBack(
    private val cm: ConnectivityManager,
    private val onConnection : () -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        cm.bindProcessToNetwork(network)
        onConnection.invoke()
    }
}