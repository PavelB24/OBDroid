package ru.barinov.obdroid.connectionsFragment

import android.bluetooth.BluetoothDevice

interface BtConnectionI {

    fun createBound()

    fun connect()
}