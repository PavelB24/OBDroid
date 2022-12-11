package ru.barinov.obdroid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.barinov.obdroid.R
import ru.barinov.obdroid.WifiConnectionWatcher
import ru.barinov.obdroid.ui.utils.ServiceCommander

class ObdService : Service() {

    companion object {
        private const val NOTIFICATION_SERVICE_ID = 88
        private const val NOTIFICATION_CHANNEL = "OBD_SERVICE_NF_CH"
        private const val NOTIFICATION_CHANNEL_NAME = "OBD service notification"
    }

    private val connectionWatcher by inject<WifiConnectionWatcher>()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        subscribeOnConnection()
    }

    private fun subscribeOnConnection() {
        serviceScope.launch {
            connectionWatcher.connectionState.onEach {  }.collect()
        }
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
            if(it.hasExtra(ServiceCommander.SERVICE_COMMAND_KEY)){
                when(it.getStringExtra(ServiceCommander.SERVICE_COMMAND_KEY)){
                    ServiceCommander.SERVICE_COMMAND_EXIT -> {stopSelf()}
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