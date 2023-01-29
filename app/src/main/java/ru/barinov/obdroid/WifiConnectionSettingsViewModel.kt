package ru.barinov.obdroid

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


    fun getConnectionState() = connectionWatcher.connectionState.value

    fun isConnectedToNetwork() =
        connectionWatcher.connectionState.value is ConnectionState.WifiConnected

    fun getGetaway() = prefs.wifiAddress

    fun getPort() = prefs.wifiPort

    fun onNewSettings(getaway: String, port: String, shouldConnect: Boolean = false) {
        setPort(port)
        setGetaway(getaway)
        if (shouldConnect) {
            connectWithWiFi()
        }
    }

    fun connectWithWiFi() {
        val lastNetwork = connectionWatcher.getCurrentWfConnection()
        if (lastNetwork != null && lastNetwork is ConnectionState.WifiConnected) {
            connectionWatcher.onChangeState(
                ConnectionState.OnAddressConfirmed(
                    lastNetwork.bssid,
                    lastNetwork.network
                )
            )
        }
    }

    private fun setGetaway(getaway: String) {
        prefs.wifiAddress = getaway
    }

    private fun setPort(port: String) {
        prefs.wifiPort = port
    }


}