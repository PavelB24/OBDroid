package ru.barinov.obdroid.activity

import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.ServiceCommander

class ActivityViewModel(
    private val commander: ServiceCommander
) : ViewModel() {

    fun stopService() = commander.stopService()

    fun startService() = commander.startService()

}