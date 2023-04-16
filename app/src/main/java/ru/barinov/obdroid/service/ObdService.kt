package ru.barinov.obdroid.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import obdKotlin.WorkMode
import obdKotlin.core.Commander
import obdKotlin.core.FailOn
import obdKotlin.core.SystemEventListener
import obdKotlin.source.BluetoothSource
import obdKotlin.source.WiFiSource
import org.koin.android.ext.android.inject
import ru.barinov.obdroid.R
import ru.barinov.obdroid.core.ObdBus
import ru.barinov.obdroid.core.ObdController
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.utils.ConnectionWatcher
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.utils.ConnectionState
import java.net.InetSocketAddress

class ObdService : Service() {

    companion object {
        private const val NOTIFICATION_SERVICE_ID = 88
        private const val NOTIFICATION_CHANNEL = "OBD_SERVICE_NF_CH"
        private const val NOTIFICATION_CHANNEL_NAME = "OBD service notification"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val controller: ObdController by inject()


    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d("@@@", "Creating")
        controller.create(serviceScope)

    }

    private fun createServiceNotification(): Notification {
//        val intent = Intent()
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.notification_title))
            .setSmallIcon(R.drawable.logo)
            .setContentText(getString(R.string.content_text_notification))
            .setDefaults(Notification.DEFAULT_ALL)
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel() {
        val serviceNotificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(serviceNotificationChannel)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_SERVICE_ID, createServiceNotification())
        intent?.let {
            if (it.hasExtra(ServiceCommander.SERVICE_COMMAND_KEY)) {
                when (it.getStringExtra(ServiceCommander.SERVICE_COMMAND_KEY)) {
                    ServiceCommander.SERVICE_COMMAND_EXIT -> {
                        stopSelf()
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

}