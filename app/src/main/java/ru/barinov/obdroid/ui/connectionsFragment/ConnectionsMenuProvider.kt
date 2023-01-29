package ru.barinov.obdroid.ui.connectionsFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import kotlinx.coroutines.*
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.R

class ConnectionsMenuProvider(
    private val btEnabler: BtEnabler,
    private val context: Context
) : MenuProvider {


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.connecions_toolbar_menu, menu)
        menu.findItem(R.id.enable_bt_item)?.isVisible = !btEnabler.isBtEnabled()
    }

    @SuppressLint("MissingPermission")
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.enable_bt_item -> {
                if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.TIRAMISU) {
                    //todo
                } else {
                    if (btEnabler.askForBt()) {

                    } else {

                    }
                }
            }
            R.id.enable_wifi_item -> {
//                wifiManager.startScan()
            }

        }
        return true
    }
}