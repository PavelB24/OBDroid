package ru.barinov.obdroid.core

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan
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

@ExperimentalCoroutinesApi
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
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

fun <T: Fragment> Fragment.getGrandParent(): T?{
    return requireParentFragment().requireParentFragment() as? T
}

fun <T: Fragment> Fragment.getParent(): T?{
    return requireParentFragment() as? T
}