package ru.barinov.obdroid

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ServiceCommander(private val context : Context) : ServiceCommandHub, AppCommandHub {

    override fun startService() {
        ContextCompat.startForegroundService(context, Intent().apply {  })
    }


}