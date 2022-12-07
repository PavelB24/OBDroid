package ru.barinov.obdroid.connectionsFragment

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Build
import android.util.Log

class WifiConnectionCallBack(
    private val onConnection : (Network) -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onConnection.invoke(network)
    }
}