package ru.barinov.obdroid.ui.uiModels


import android.bluetooth.BluetoothClass
import kotlinx.android.parcel.Parcelize
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.ui.connectionsFragment.BtConnectionI

data class BtConnectionItem(
    override val type : ConnectionType,
    val rssi: Short?,
    val btClass: BluetoothClass?,
    val address : String,
    val name : String?,
    val boundState : Int,
    val actions : BtConnectionI,
) : ConnectionItem(type) {

     companion object {
        const val BT_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    fun toItemWithExtractedSocket(): BtItem {
       return BtItem(
            type,
            address,
            name,
            boundState
        )
    }
}