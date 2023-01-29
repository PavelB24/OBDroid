package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.net.Socket

interface BtConnectionI {

    @Throws(IOException::class)
    fun createBound() : Boolean

    @Throws(IOException::class)
    fun connect() : BluetoothSocket?
}