package ru.barinov.obdroid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import obdKotlin.WorkMode
import obdKotlin.core.Commander
import obdKotlin.core.FailOn
import obdKotlin.core.SystemEventListener
import obdKotlin.source.BluetoothSource
import org.koin.android.ext.android.inject
import ru.barinov.obdroid.R
import ru.barinov.obdroid.core.ObdEventBus
import ru.barinov.obdroid.utils.ConnectionWatcher
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.utils.ConnectionState

class ObdService : Service() {

    companion object {
        private const val NOTIFICATION_SERVICE_ID = 88
        private const val NOTIFICATION_CHANNEL = "OBD_SERVICE_NF_CH"
        private const val NOTIFICATION_CHANNEL_NAME = "OBD service notification"
    }

    private val connectionWatcher by inject<ConnectionWatcher>()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val commander =  Commander.Builder()
        .setEventListener(object: SystemEventListener{
            override fun onConnect(source: SystemEventListener.SourceType) {
                Log.d("@@@", "Connected")
                serviceScope.launch {
                    ObdEventBus.onStateChanged(ObdEventBus.ObdEvents.SuccessConnect)
                }
                start()
            }

            override fun onDecodeError(fail: FailOn?) {

            }

            override fun onSourceError(source: SystemEventListener.SourceType) {
                Log.d("@@@", "ERR CONNECT ")
            }

            override fun onSwitchMode(extendedMode: Boolean) {

            }

            override fun onWorkModeChanged(workMode: WorkMode) {
                Log.d("@@@", workMode.name)
            }
        }).enableRawData().build()

    private fun start() {
        serviceScope.launch {
            commander.rawDataFlow.onEach {
                Log.d("@@@", it.toString())
            }.collect()
        }
        commander.startWithAuto()
    }

    override fun onBind(intent: Intent?): IBinder? = null



    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        subscribeOnConnection()
    }

    private fun subscribeOnConnection() {
        serviceScope.launch {
            connectionWatcher.connectionState.onEach {
                when(it){
                    is ConnectionState.WifiConnected -> {}
                    is ConnectionState.BtSocketObtained -> {
                        it.socket?.let { socket ->
                            commander.bindSource(BluetoothSource(socket))
                        }
                    }
                    is ConnectionState.LinkPropertiesChanged -> {}
                    is ConnectionState.Lost -> {}
                    is ConnectionState.OnAddressConfirmed -> {}
                    ConnectionState.UnAvailable -> {}
                }
            }.collect()
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