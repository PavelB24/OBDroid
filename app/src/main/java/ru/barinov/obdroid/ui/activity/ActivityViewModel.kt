package ru.barinov.obdroid.ui.activity

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.core.ObdBus
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.preferences.Preferences

class ActivityViewModel(
    private val commander: ServiceCommander,
    private val preferences: Preferences
) : ViewModel() {

    fun stopService() = commander.stopService()

    fun startService() = commander.startService()


    fun shouldShowTerminal() = preferences.useTerminal

    fun isObdConnected(): Boolean{
        return ObdBus.lastEvent != null &&
                ObdBus.lastEvent == ObdBus.ObdEvents.SuccessConnect
    }

}