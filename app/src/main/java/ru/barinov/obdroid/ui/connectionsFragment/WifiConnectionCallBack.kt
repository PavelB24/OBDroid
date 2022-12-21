package ru.barinov.obdroid.ui.connectionsFragment

import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import ru.barinov.obdroid.ConnectionState
import ru.barinov.obdroid.ConnectionWatcher

class WifiConnectionCallBack(
    private val connectionWatcher : ConnectionWatcher,
    private val onConnection : (Network) -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onConnection.invoke(network)
        connectionWatcher.onChangeNetworkState(ConnectionState.Connected(network))
    }

    override fun onUnavailable() {
        super.onUnavailable()
        connectionWatcher.onChangeNetworkState(ConnectionState.UnAvailable)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        connectionWatcher.onChangeNetworkState(
            ConnectionState.LinkPropertiesChanged(
                network,
                linkProperties
            )
        )
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        connectionWatcher.onChangeNetworkState(ConnectionState.Lost(network))
    }
}