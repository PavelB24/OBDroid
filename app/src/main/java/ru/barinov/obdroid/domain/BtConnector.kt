package ru.barinov.obdroid.domain

import android.bluetooth.BluetoothSocket
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

interface BtConnector {

    fun connectBt(
        address: String,
        socket: BluetoothSocket
    )

    fun connectBt(item: BtConnectionItem)
}