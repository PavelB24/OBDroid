package ru.barinov.obdroid

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.preferences.Preferences

class WifiConnectionSettingsViewModel(
    private val prefs: Preferences,
    private val connectionWatcher: WifiConnectionWatcher
) : ViewModel() {


    fun getConnectionState() = connectionWatcher.connectionState.value

    fun isConnectedToNetwork() =
        connectionWatcher.connectionState.value is WifiConnectionState.Connected




}