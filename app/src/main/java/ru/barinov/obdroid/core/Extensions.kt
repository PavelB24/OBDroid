package ru.barinov.obdroid.core

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.ui.connectionsFragment.BtConnectionI
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBtConnectionItem(actions : BtConnectionI) : BtConnectionItem {
    return BtConnectionItem(
        ConnectionItem.ConnectionType.BLUETOOTH,
        address,
        name,
        bondState,
        actions
    )
}