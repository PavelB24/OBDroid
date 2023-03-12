package ru.barinov.obdroid.ui.dashBoardFragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import ru.barinov.obdroid.base.ConnectionStateItemHolder

class DashBoardMenuProvider: MenuProvider, ConnectionStateItemHolder() {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.inflateAndManageAnimation(menu, menuInflater, null)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
       return true
    }
}