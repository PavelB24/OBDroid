package ru.barinov.obdroid.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import obdKotlin.WorkMode
import obdKotlin.core.Commander
import obdKotlin.core.FailOn
import obdKotlin.core.SystemEventListener
import obdKotlin.messages.Message
import obdKotlin.source.BluetoothSource
import obdKotlin.source.WiFiSource
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.utils.ConnectionState
import ru.barinov.obdroid.utils.ConnectionWatcher
import java.net.InetSocketAddress

class ObdController(
    private val prefs: Preferences,
    private val connectionWatcher: ConnectionWatcher
) {

    private var core: Commander? = null
    private var observeJob: Job? = null


    fun create(scope: CoroutineScope): Boolean {
        return if (core == null) {
            val listener = CoreEventListener(scope)
            core = Commander.Builder().setEventListener(listener).enableRawData().build()
            observeConnections(scope)
            true
        } else false

    }

    private fun observeConnections(scope: CoroutineScope) {
        observeJob?.cancel()
        observeJob = scope.launch {
            connectionWatcher.connectionState.onEach {
                when (it) {
                    is ConnectionState.BtSocketObtained -> {
                        core?.bindSource(BluetoothSource(it.socket))
                    }
                    is ConnectionState.LinkPropertiesChanged -> TODO()
                    is ConnectionState.Lost -> TODO()
                    is ConnectionState.OnAddressConfirmed -> TODO()
                    ConnectionState.OnQuickWiFiSetUp -> {
                        core?.bindSource(
                            WiFiSource(
                                InetSocketAddress(
                                    prefs.wifiAddress,
                                    prefs.wifiPort
                                )
                            )
                        )
                    }
                    ConnectionState.UnAvailable -> TODO()
                    is ConnectionState.WifiConnected -> {

                    }
                }
            }.collect()
        }
    }

    private fun onStart(){

    }

    private fun startAuto() {

    }


    fun getMessagesFlow(): SharedFlow<Message?> {
        return core?.encodedDataMessages
            ?: throw IllegalStateException("Core is not initialised yet")
    }

    fun getRawFlow(): SharedFlow<String?> {
        return core?.rawDataFlow ?: throw IllegalStateException("Core is not initialised yet")
    }


    fun reset(scope: CoroutineScope) {
        core = null
        create(scope)
    }


    fun disconnect() {
        core?.disconnect()
    }


    inner class CoreEventListener(
        private val scope: CoroutineScope
    ) : SystemEventListener {

        override fun onConnect(source: SystemEventListener.SourceType) {
            scope.launch {
                ObdBus.onStateChanged(ObdBus.ObdEvents.SuccessConnect)
            }
        }

        override fun onDecodeError(fail: FailOn?) {
        }

        override fun onSourceError(source: SystemEventListener.SourceType) {
            scope.launch {
                ObdBus.onStateChanged(ObdBus.ObdEvents.ConnectionFailed())
            }
        }

        override fun onSwitchMode(extendedMode: Boolean) {
        }

        override fun onWorkModeChanged(workMode: WorkMode) {
        }
    }


}