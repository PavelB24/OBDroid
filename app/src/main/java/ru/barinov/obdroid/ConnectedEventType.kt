package ru.barinov.obdroid

import android.bluetooth.BluetoothSocket
import android.net.Network
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem

sealed class ConnectedEventType {

    data class WifiConnected(
        val item: WifiConnectionItem
    ) : ConnectedEventType()

    data class BluetoothConnected(
        val item: BtConnectionItem
    ) : ConnectedEventType()

    object Fail : ConnectedEventType()
}

