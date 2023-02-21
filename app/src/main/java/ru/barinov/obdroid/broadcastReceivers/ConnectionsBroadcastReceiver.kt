package ru.barinov.obdroid.broadcastReceivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.barinov.obdroid.ConnectionReceiverEvent

class ConnectionsBroadcastReceiver() : BroadcastReceiver() {

    private val _receiverEvents = MutableSharedFlow<ConnectionReceiverEvent>()
    val receiverEvents: SharedFlow<ConnectionReceiverEvent> = _receiverEvents

    private val receiverScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        receiverScope.launch {
            when (intent?.action) {
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        _receiverEvents.emit(ConnectionReceiverEvent.ScanAvailable)
                    }
                }

                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )
                    when (state) {
                        BluetoothAdapter.STATE_OFF,
                        BluetoothAdapter.STATE_ON -> {
                            receiverScope.launch {
                                _receiverEvents.emit(ConnectionReceiverEvent.AdapterStateChanged)
                            }
                        }
                    }
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = getDevice(intent)
                    device?.let {
                        _receiverEvents.emit(
                            ConnectionReceiverEvent.NewBtDeviceFound(
                                it,
                                getRssi(intent),
                                getClass(intent)
                            )
                        )
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val currentState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0)
                    val prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, 0)
                    val device: BluetoothDevice? = getDevice(intent)
                    handleBoundStateChange(
                        currentState,
                        prevState,
                        device,
                        getRssi(intent),
                        getClass(intent)
                    )
                }
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                    Log.d("@@@", "Conn Changed")
                }
            }
        }
    }

    private fun getDevice(intent: Intent): BluetoothDevice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
    }

    private fun getRssi(intent: Intent): Short {
        return intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, 0)
    }

    private fun getClass(intent: Intent): BluetoothClass? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                BluetoothDevice.EXTRA_CLASS,
                BluetoothClass::class.java
            )
        } else {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS)
        }
    }

    private suspend fun handleBoundStateChange(
        currentState: Int,
        prevState: Int,
        device: BluetoothDevice?,
        rssi: Short?,
        clazz: BluetoothClass?
    ) {
        when {
            currentState == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BluetoothBounded(it, rssi, clazz))
                }
            }
            currentState == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_NONE -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BluetoothBounded(it, rssi, clazz))
                }
            }
//            currentState == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING -> {
//                device?.let {
//                    _receiverEvents.emit(ConnectionReceiverEvent.ReBound(it))
//                }
//            }
            currentState == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.UnBounded(it, rssi, clazz))
                }
            }
            currentState == BluetoothDevice.BOND_BONDING && prevState == BluetoothDevice.BOND_NONE -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BoundingStarted)
                }
            }
            currentState == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDING -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BoundFailed(it, rssi, clazz))
                }
            }
        }
    }
}