package ru.barinov.obdroid

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class ServiceCommander(private val context : Context) {

    companion object{
        const val SERVICE_COMMAND_KEY = "s_command"
        const val SERVICE_COMMAND_EXIT = "on_exit"
    }


     fun startService() {
        ContextCompat.startForegroundService(context, Intent(context, ObdService::class.java))
    }

     fun stopService() {
         ContextCompat.startForegroundService(
             context,
             Intent(
                 context,
                 ObdService::class.java
             ).apply {
                 putExtra(SERVICE_COMMAND_KEY, SERVICE_COMMAND_EXIT)
             }
         )
    }

}