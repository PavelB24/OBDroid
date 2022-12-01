package ru.barinov.obdroid.uiModels

import android.bluetooth.BluetoothDevice
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.connectionsFragment.BtConnectionI

data class BtConnectionItem(
    override val type : ConnectionType,
    val address : String,
    val name : String?,
    val boundState : Int,
    val actions : BtConnectionI,
) : ConnectionItem(type)