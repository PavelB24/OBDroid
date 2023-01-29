package ru.barinov.obdroid.ui.settings

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.preferences.Preferences

class SettingsFragmentViewModel(
    private val prefs: Preferences
) : ViewModel() {

    fun getTerminalFlag() = prefs.useTerminal

    fun changeTerminalEnabled(flag : Boolean){
        prefs.useTerminal = flag
    }

    fun changeUseWarmStarts(checked: Boolean) {
        prefs.useWarmStarts = checked
    }

    fun getWarmStartsFlag(): Boolean = prefs.useWarmStarts


    fun getUseOnlySupported(): Boolean =  prefs.showOnlySupported

    fun changeUseOnlySupported(checked: Boolean) {
        prefs.showOnlySupported = checked
    }

    fun saveShellThemeIdByOrdinal(position: Int) {
        prefs.savedShellThemeId = position
    }

    fun getSavedThemeId(): Int = prefs.savedShellThemeId
}