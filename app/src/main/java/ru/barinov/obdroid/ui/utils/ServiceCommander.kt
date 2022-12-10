package ru.barinov.obdroid.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import ru.barinov.obdroid.service.ObdService

class ServiceCommander(private val context : Context) {

    companion object{
        const val SERVICE_COMMAND_KEY = "s_command"
        const val SERVICE_COMMAND_EXIT = "on_exit"
        const val SERVICE_COMMAND_WIFI_PARAMS = "wf_params"
    }


     fun startService() {
        ContextCompat.startForegroundService(context, Intent(context, ObdService::class.java))
    }

     fun stopService() {
        sendMessageToService(SERVICE_COMMAND_EXIT)
    }

    fun onWifiParamsChanged(){
        sendMessageToService(SERVICE_COMMAND_WIFI_PARAMS)
    }


    private fun sendMessageToService(extra : String){
        ContextCompat.startForegroundService(
            context,
            Intent(
                context,
                ObdService::class.java
            ).apply {
                putExtra(SERVICE_COMMAND_KEY, extra)
            }
        )
    }

}