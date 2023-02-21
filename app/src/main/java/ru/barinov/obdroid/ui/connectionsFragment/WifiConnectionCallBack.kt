package ru.barinov.obdroid.ui.connectionsFragment

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.InetAddresses
import android.net.LinkProperties
import android.net.Network
import ru.barinov.obdroid.utils.ConnectionState
import ru.barinov.obdroid.utils.ConnectionWatcher
import java.net.InetSocketAddress
import java.net.SocketAddress

class WifiConnectionCallBack(
    private val connectionWatcher : ConnectionWatcher,
    private val bssid : String,
    private val connManager: ConnectivityManager,
    private val onConnection : (Boolean, Network) -> Unit
) : NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        val result = connManager.bindProcessToNetwork(network)
        network.socketFactory.createSocket().also {
            it.connect(InetSocketAddress("192.168.1.102", 35355))
            it.getOutputStream().write("HELLO FROM PHONE".encodeToByteArray())
        }
        onConnection.invoke(result, network)
        connectionWatcher.onChangeState(ConnectionState.WifiConnected(bssid, network))
    }

    override fun onUnavailable() {
        super.onUnavailable()
        connectionWatcher.onChangeState(ConnectionState.UnAvailable)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        connectionWatcher.onChangeState(
            ConnectionState.LinkPropertiesChanged(
                bssid,
                network,
                linkProperties
            )
        )
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        connManager.bindProcessToNetwork(null)
        connManager.unregisterNetworkCallback(this)
        connectionWatcher.onChangeState(ConnectionState.Lost(network))
    }
}