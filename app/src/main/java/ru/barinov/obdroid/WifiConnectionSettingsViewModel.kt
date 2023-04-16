package ru.barinov.obdroid

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.utils.ConnectionState
import ru.barinov.obdroid.utils.ConnectionWatcher

class WifiConnectionSettingsViewModel(
    private val prefs: Preferences,
    private val connectionWatcher: ConnectionWatcher,
    private val serviceCommander: ServiceCommander
) : ViewModel() {


    fun getConnectionState() = connectionWatcher.lastState

    fun isConnectedToNetwork() =
        connectionWatcher.lastState is ConnectionState.WifiConnected ||
                connectionWatcher.lastState is ConnectionState.LinkPropertiesChanged

    fun getGetaway() = prefs.wifiAddress

    fun getPort() = prefs.wifiPort

    fun onNewSettings(
        getaway: String,
        port: String,
        shouldConnect: Boolean,
        quickSetUp: Boolean
    ) {
        setPort(port)
        setGetaway(getaway)
        if(quickSetUp){
            quickWiFiConnect()
        } else {
            if (shouldConnect) {
                Log.d("@@@", "Connecting....")
                connectWithWiFi()
            }
        }
    }

    private fun quickWiFiConnect() {
        connectionWatcher.onChangeState(ConnectionState.OnQuickWiFiSetUp)
    }

    fun connectWithWiFi() {
        val lastNetwork = connectionWatcher.getCurrentWfConnection()
        if (lastNetwork != null) {
            val networkData = when (lastNetwork) {
                is ConnectionState.WifiConnected -> {
                    Pair(lastNetwork.network, lastNetwork.bssid)
                }
                is ConnectionState.LinkPropertiesChanged -> {
                    Pair(lastNetwork.network, lastNetwork.bssid)
                }
                else -> null
            }
            networkData?.let {
                connectionWatcher.onChangeState(
                    ConnectionState.OnAddressConfirmed(
                        it.second,
                        it.first
                    )
                )
            }
        }
    }

    private fun setGetaway(getaway: String) {
        prefs.wifiAddress = getaway
    }

    private fun setPort(port: String) {
        prefs.wifiPort = Integer.valueOf(port)
    }


}