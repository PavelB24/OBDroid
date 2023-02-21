package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothSocket

interface SimpleConnectionI {

   fun onBtSelected(
      address: String,
      socket: BluetoothSocket
   )

   fun onWiFiSelected()
}