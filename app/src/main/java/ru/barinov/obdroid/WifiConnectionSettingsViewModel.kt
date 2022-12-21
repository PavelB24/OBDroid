package ru.barinov.obdroid

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.utils.ServiceCommander

class WifiConnectionSettingsViewModel(
    private val prefs: Preferences,
    private val connectionWatcher: ConnectionWatcher,
    private val serviceCommander: ServiceCommander
) : ViewModel() {


    fun getConnectionState() = connectionWatcher.connectionState.value

    fun isConnectedToNetwork() =
        connectionWatcher.connectionState.value is ConnectionState.Connected

    fun getGetaway() = prefs.wifiAddress

    fun getPort() = prefs.wifiPort

    fun onNewSettings(getaway: String, port: String){
        setPort(port)
        setGetaway(getaway)
        serviceCommander.onWifiParamsChanged()
    }

    private fun setGetaway(getaway: String) {
        prefs.wifiAddress = getaway
    }

    private fun setPort(port: String) {
        prefs.wifiPort = port
    }


}