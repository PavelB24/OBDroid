package ru.barinov.obdroid.connectionsFragment

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.os.Build
import android.util.Log
import ru.barinov.obdroid.WifiConnectionState
import ru.barinov.obdroid.WifiConnectionWatcher

class WifiConnectionCallBack(
    private val wifiConnectionWatcher : WifiConnectionWatcher,
    private val onConnection : (Network) -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onConnection.invoke(network)
        wifiConnectionWatcher.onChangeNetworkState(WifiConnectionState.Connected(network))
    }

    override fun onUnavailable() {
        super.onUnavailable()
        wifiConnectionWatcher.onChangeNetworkState(WifiConnectionState.UnAvailable)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        wifiConnectionWatcher.onChangeNetworkState(
            WifiConnectionState.LinkPropertiesChanged(
                network,
                linkProperties
            )
        )
    }


    override fun onLost(network: Network) {
        super.onLost(network)
        wifiConnectionWatcher.onChangeNetworkState(WifiConnectionState.Lost(network))
    }
}