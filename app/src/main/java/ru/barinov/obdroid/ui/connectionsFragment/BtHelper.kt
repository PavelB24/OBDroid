package ru.barinov.obdroid.ui.connectionsFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class BtHelper(
    private val fragment: Fragment,
    private val btManager: BluetoothManager,
) {

    @SuppressLint("MissingPermission")
    private var btLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (ActivityCompat.checkSelfPermission(
                        fragment.requireContext(),
                        Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    btManager.adapter?.startDiscovery()
                }
            }
        }

    fun askForBt(): Boolean {
        return if (btManager.adapter?.isEnabled == false) {
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
             true
        } else false

    }

    fun getRemoteDevice(address: String) = btManager.adapter.getRemoteDevice(address)

    fun getPairedDevices(): Set<BluetoothDevice>? {
        return if (ActivityCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            btManager.adapter?.bondedDevices
        } else null
    }

    fun getAdapter(): BluetoothAdapter? = btManager.adapter



    fun isBtEnabled() = btManager.adapter?.isEnabled == true
}