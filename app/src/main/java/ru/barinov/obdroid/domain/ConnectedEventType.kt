package ru.barinov.obdroid.domain

import ru.barinov.obdroid.ui.uiModels.BtItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem

sealed class ConnectedEventType {

    data class WifiConnected(
        val item: WifiConnectionItem
    ) : ConnectedEventType()

    data class BluetoothConnecting(
        val item: BtItem
    ) : ConnectedEventType()

    object Fail : ConnectedEventType()
}

