package ru.barinov.obdroid.core

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import obdKotlin.WorkMode
import obdKotlin.core.Commander
import obdKotlin.core.FailOn
import obdKotlin.core.SystemEventListener
import obdKotlin.messages.Message
import obdKotlin.protocol.Protocol
import obdKotlin.source.BluetoothSource
import obdKotlin.source.NonBlockingWiFiSource
import obdKotlin.source.WiFiSource
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.data.ProfilesRepository
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.utils.ConnectionState
import ru.barinov.obdroid.utils.ConnectionWatcher
import java.net.InetSocketAddress

class ObdController(
    private val prefs: Preferences,
    private val connectionWatcher: ConnectionWatcher,
    private val profilesRepository: ProfilesRepository
): RawDataProvider {

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
        Log.d("@@@", "Observe....")
        observeJob?.cancel()
        observeJob = scope.launch {
            connectionWatcher.connectionState.onEach {
                when (it) {
                    is ConnectionState.BtSocketObtained -> {
                        core?.let { obd ->
                            obd.bindSource(
                                BluetoothSource(
                                    it.socket
                                )
                            )
                            onStart()
                        }
                    }
                    is ConnectionState.LinkPropertiesChanged -> {}
                    is ConnectionState.Lost -> {}
                    is ConnectionState.OnAddressConfirmed -> {
                        Log.d("@@@", "OnAddressConfirmed....")
                        core?.let { obd->
                            obd.bindSource(
                                WiFiSource(
                                    it.network.socketFactory.createSocket(
                                        "192.168.1.113",
                                        35355
//                                        /* host = */ "${prefs.wifiAddress}",
//                                        /* port = */ prefs.wifiPort
                                    )
                                )
                            )
                            onStart()
                        }

                    }

                    ConnectionState.OnQuickWiFiSetUp -> {
                        core?.let { obd ->
                            obd.bindSource(
                                NonBlockingWiFiSource(
                                    InetSocketAddress(
                                        prefs.wifiAddress,
                                        prefs.wifiPort
                                    )
                                )
                            )
                            onStart()
                        }
                    }
                    ConnectionState.UnAvailable -> {}
                    is ConnectionState.WifiConnected -> {

                    }
                }
            }.collect()
        }
    }

    private suspend fun onStart()  = core?.apply {
        profilesRepository.getSelectedProfile().let {
            if (Protocol.values()[it.protocol] == Protocol.AUTOMATIC) {
                startWithAuto(commandCsvToList(it.atCommandChain))
            } else {
                start(
                    Protocol.values()[it.protocol],
                    commandCsvToList(it.atCommandChain)
                )
            }
        }
    }


    private fun commandCsvToList(csv: String?): List<String>?{
        return try{
            csv?.split(",")
        }catch (e: Exception){
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            null
        }
    }


    fun getMessagesFlow(): SharedFlow<Message?> {
        return core?.encodedDataMessages
            ?: throw IllegalStateException("Core is not initialised yet")
    }

    override fun getRawDataFlow(): SharedFlow<String?> {
        return core?.rawDataFlow ?: throw IllegalStateException("Core is not initialised yet")
    }

    override fun sendCommand(command: String) {
        core?.sendCommand(command)
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
            Log.d("@@@", "$workMode")
        }
    }


}