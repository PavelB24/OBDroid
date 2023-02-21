package ru.barinov.obdroid

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice

sealed class ConnectionReceiverEvent {

    data class BluetoothBounded(
        val device: BluetoothDevice,
        val rssi: Short?,
        val clazz: BluetoothClass?
    ) : ConnectionReceiverEvent()

    data class BoundFailed(
        val device: BluetoothDevice,
        val rssi: Short?,
        val clazz: BluetoothClass?
    ) : ConnectionReceiverEvent()

    data class UnBounded(
        val device: BluetoothDevice,
        val rssi: Short?,
        val clazz: BluetoothClass?
    ) : ConnectionReceiverEvent()

    data class ReBound(
        val device: BluetoothDevice,
        val rssi: Short?,
        val clazz: BluetoothClass?
    ) : ConnectionReceiverEvent()

    data class NewBtDeviceFound(
        val device: BluetoothDevice,
        val rssi: Short?,
        val clazz: BluetoothClass?
    ) : ConnectionReceiverEvent()

    object ScanAvailable : ConnectionReceiverEvent()

    object BoundingStarted : ConnectionReceiverEvent()

    object AdapterStateChanged : ConnectionReceiverEvent()

}
