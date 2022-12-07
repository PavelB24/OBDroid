package ru.barinov.obdroid

import android.bluetooth.BluetoothSocket
import android.net.Network
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

sealed class ConnectedEventType {

    data class WifiConnected(
        val network: Network,
        val item: WifiConnectionItem
    ) : ConnectedEventType()

    data class BluetoothConnected(
        val socket: BluetoothSocket,
        val item: BtConnectionItem
    ) : ConnectedEventType()


    object Fail : ConnectedEventType()
}

