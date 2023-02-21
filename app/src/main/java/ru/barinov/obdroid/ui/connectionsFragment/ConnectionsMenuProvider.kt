package ru.barinov.obdroid.ui.connectionsFragment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import kotlinx.coroutines.*
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.R
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import java.util.*

class ConnectionsMenuProvider(
    private val btHelper: BtHelper,
    private val context: Context,
    private val simpleConnectionI: SimpleConnectionI
) : MenuProvider {


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.connecions_toolbar_menu, menu)
        menu.findItem(R.id.enable_bt_item)?.isVisible = !btHelper.isBtEnabled()
    }

    @SuppressLint("MissingPermission")
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.enable_bt_item -> {
                if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.TIRAMISU) {
                    //todo
                } else {
                    if (btHelper.askForBt()) {

                    } else {

                    }
                }
            }

            R.id.bt_connect -> {
                if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    showPairs(btHelper.getPairedDevices())
                } else Toast.makeText(
                    context,
                    context.getString(R.string.no_paired_devices_yet),
                    Toast.LENGTH_LONG
                ).show()
            }

            R.id.wf_connect -> {
                simpleConnectionI.onWiFiSelected()
            }

        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun showPairs(devices: Set<BluetoothDevice>?) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            android.R.layout.select_dialog_singlechoice,
            devices?.map { it.name.ifEmpty { it.address } }?.toTypedArray() ?: arrayOf()
        )

        val addresses = devices?.map { it.address }

        alertDialog.setSingleChoiceItems(
            adapter,
            -1
        ) { dialog, which ->
            dialog.dismiss()
            val position: Int = (dialog as AlertDialog).listView.checkedItemPosition
            addresses?.let {
                val deviceAddress: String = it[position]
                val device = btHelper.getRemoteDevice(deviceAddress)
                val socket = device.createInsecureRfcommSocketToServiceRecord(
                    UUID.fromString(BtConnectionItem.BT_UUID)
                )
                simpleConnectionI.onBtSelected(
                    device.address,
                    socket
                )

            }
        }
        alertDialog.setTitle(context.getString(R.string.bound_bt_title))
        alertDialog.show()
    }
}