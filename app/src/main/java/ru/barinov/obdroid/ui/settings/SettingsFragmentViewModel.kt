package ru.barinov.obdroid.ui.settings

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.preferences.Preferences

class SettingsFragmentViewModel(
    private val prefs : Preferences
) : ViewModel() {

    fun getTerminalFlag() = prefs.useTerminal

    fun changeTerminalEnabled(flag : Boolean){
        prefs.useTerminal = flag
    }
}