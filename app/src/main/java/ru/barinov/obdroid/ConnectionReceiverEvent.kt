package ru.barinov.obdroid

import android.bluetooth.BluetoothDevice
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

sealed class ConnectionReceiverEvent{

    data class BluetoothBounded(val device: BluetoothDevice) : ConnectionReceiverEvent()

    data class BoundFailed(val device: BluetoothDevice) : ConnectionReceiverEvent()

    data class UnBounded(val device: BluetoothDevice) : ConnectionReceiverEvent()

    data class ReBound(val device: BluetoothDevice) : ConnectionReceiverEvent()

    data class NewBtDeviceFound(val device: BluetoothDevice) : ConnectionReceiverEvent()

    object ScanAvailable : ConnectionReceiverEvent()

    object BoundingStarted : ConnectionReceiverEvent()

}
