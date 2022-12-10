package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.net.Socket

interface BtConnectionI {

    fun createBound() : Boolean

    fun connect() : BluetoothSocket?
}