package ru.barinov.obdroid.connectionsFragment

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Build

class WifiConnectionCallBack(val cm: ConnectivityManager) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        // To make sure that requests don't go over mobile data
        cm.bindProcessToNetwork(network)
    }
}