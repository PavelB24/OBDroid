package ru.barinov.obdroid.ui.troublesFragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.CommonMenuInflater

class TroublesMenuProvider(private val navController: NavController): MenuProvider, CommonMenuInflater {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.inflateAndManageAnimation(menu, menuInflater, R.menu.troubles_toolbar_menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.recent_troubles -> {
                navController.navigate(
                    R.id.action_diagnosticFragment_to_troubleHistory
                )
            }
        }
        return true
    }
}