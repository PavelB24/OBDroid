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
import kotlinx.coroutines.launch
import ru.barinov.obdroid.ConnectionReceiverEvent

class ConnectionsBroadcastReceiver() : BroadcastReceiver() {


    val receiverEvents = MutableSharedFlow<ConnectionReceiverEvent>()

    private val receiverScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    receiverScope.launch {
                        receiverEvents.emit(ConnectionReceiverEvent.ScanAvailable)
                    }
                    doOnNewScanResults()
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
                receiverScope.launch {
                    receiverEvents.emit(ConnectionReceiverEvent.NewBtDeviceFound(device))
                }
                addDevice(device)
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
                Log.d("@@@", "State Changed $currentState $prevState")
            }
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                Log.d("@@@", "Conn Changed")
            }
        }
    }

    private fun handleBoundStateChange(
        currentState: Int,
        prevState: Int,
        device: BluetoothDevice?) {

    }
}