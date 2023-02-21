package ru.barinov.obdroid.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ru.barinov.obdroid.service.ObdService

class ServiceCommander(private val context : Context) {

    companion object{
        const val SERVICE_COMMAND_KEY = "s_command"
        const val SERVICE_COMMAND_EXIT = "on_exit"
    }


     fun startService() {
        sendMessageToService(SERVICE_COMMAND_KEY)
    }

     fun stopService() {
        sendMessageToService(SERVICE_COMMAND_EXIT)
    }

    private fun sendMessageToService(extra : String){
        ContextCompat.startForegroundService(
            context,
            Intent(
                context,
                ObdService::class.java
            ).also {
                it.putExtra(SERVICE_COMMAND_KEY, extra)
            }
        )
    }

}