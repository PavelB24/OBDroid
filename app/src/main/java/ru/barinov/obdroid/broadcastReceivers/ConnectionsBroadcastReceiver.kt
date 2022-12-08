package ru.barinov.obdroid.broadcastReceivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.barinov.obdroid.ConnectionReceiverEvent

class ConnectionsBroadcastReceiver() : BroadcastReceiver() {


    private val _receiverEvents = MutableSharedFlow<ConnectionReceiverEvent>()
    val receiverEvents : SharedFlow<ConnectionReceiverEvent> = _receiverEvents

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
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    device?.let {
                        _receiverEvents.emit(ConnectionReceiverEvent.NewBtDeviceFound(it))
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val currentState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0)
                    val prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, 0)
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    handleBoundStateChange(currentState, prevState, device)
                }
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                    Log.d("@@@", "Conn Changed")
                }
            }
        }
    }

    private suspend fun handleBoundStateChange(
        currentState: Int,
        prevState: Int,
        device: BluetoothDevice?
    ) {
        when {
            currentState == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_NONE ->{
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.UnBounded(it))
                }
            }
            currentState == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.ReBound(it))
                }
            }
            currentState == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED -> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BluetoothBounded(it))
                }
            }
            currentState == BluetoothDevice.BOND_BONDING  && prevState == BluetoothDevice.BOND_NONE-> {
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BoundingStarted)
                }
            }
            currentState == BluetoothDevice.BOND_NONE &&  prevState == BluetoothDevice.BOND_BONDING ->{
                device?.let {
                    _receiverEvents.emit(ConnectionReceiverEvent.BoundFailed(it))
                }
            }
        }
    }
}