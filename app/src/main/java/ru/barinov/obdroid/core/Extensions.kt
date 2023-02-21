package ru.barinov.obdroid.core

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.ui.connectionsFragment.BtConnectionI
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBtConnectionItem(
    rssi: Short?,
    clazz: BluetoothClass?,
    actions : BtConnectionI) : BtConnectionItem {
    return BtConnectionItem(
        ConnectionItem.ConnectionType.BLUETOOTH,
        rssi,
        clazz,
        address,
        name,
        bondState,
        actions
    )
}

fun Toolbar.setupWithNavController(
    navController: NavController,
    drawerLayout: DrawerLayout?,
    fragmentIds: Set<Int>
) {
    NavigationUI.setupWithNavController(
        this, navController,
        AppBarConfiguration(fragmentIds, drawerLayout)
    )
}