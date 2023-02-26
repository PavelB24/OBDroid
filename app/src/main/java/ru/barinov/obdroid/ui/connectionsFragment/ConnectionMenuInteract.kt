package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothSocket

interface ConnectionMenuInteract {

   fun onBtSelected(
      address: String,
      socket: BluetoothSocket
   )

   fun onWiFiSelected()
}