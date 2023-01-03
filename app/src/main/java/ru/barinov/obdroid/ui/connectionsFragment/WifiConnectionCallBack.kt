package ru.barinov.obdroid.ui.connectionsFragment

import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import ru.barinov.obdroid.utils.ConnectionState
import ru.barinov.obdroid.utils.ConnectionWatcher

class WifiConnectionCallBack(
    private val connectionWatcher : ConnectionWatcher,
    private val onConnection : (Network) -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onConnection.invoke(network)
        connectionWatcher.onChangeState(ConnectionState.WifiConnected(network))
    }

    override fun onUnavailable() {
        super.onUnavailable()
        connectionWatcher.onChangeState(ConnectionState.UnAvailable)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        connectionWatcher.onChangeState(
            ConnectionState.LinkPropertiesChanged(
                network,
                linkProperties
            )
        )
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        connectionWatcher.onChangeState(ConnectionState.Lost(network))
    }
}