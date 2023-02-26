package ru.barinov.obdroid.ui.sensorsFragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.core.view.get
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.CommonMenuInflater
import ru.barinov.obdroid.domain.CommandCategory

class SensorsFragmentMenuProvider(
    private val viewModel: SensorsViewModel
) : MenuProvider, CommonMenuInflater {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.inflateAndManageAnimation(menu, menuInflater, R.menu.sensors_toolbar_menu)
        menu.getItem(1).isChecked = viewModel.getSavedFavsState()
        val savedSortItem = when (viewModel.getSavedSortState()) {
            1 -> 1
            4 -> 3
            10 -> 4
            5 -> 5
            6 -> 6
            9 -> 7
            11 -> 8
            12 -> 9
            15 -> 2
            else -> 0
        }
        menu.getItem(0).subMenu?.get(savedSortItem)?.isChecked = true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = !menuItem.isChecked
        when (menuItem.itemId) {
            R.id.cat_all -> viewModel.changeCategory(CommandCategory.UNDEFINED)
            R.id.cat_diesel -> viewModel.changeCategory(CommandCategory.DIESEL)
            R.id.cat_electric -> viewModel.changeCategory(CommandCategory.ELECTRICAL)
            R.id.cat_throttle -> viewModel.changeCategory(CommandCategory.THROTTLE)
            R.id.cat_air -> viewModel.changeCategory(CommandCategory.AIR_FLOW)
            R.id.cat_fuel_injection -> viewModel.changeCategory(CommandCategory.FUEL_INJECTION)
            R.id.cat_temperature -> viewModel.changeCategory(CommandCategory.TEMPERATURE)
            R.id.cat_oxygen -> viewModel.changeCategory(CommandCategory.OXYGEN_SENSOR)
            R.id.cat_info -> viewModel.changeCategory(CommandCategory.VEHICLE_INFO)
            R.id.cat_turbo -> viewModel.changeCategory(CommandCategory.TURBO_CHARGER)
            R.id.fav_toolbar_button -> viewModel.setShowFavs(menuItem.isChecked)
        }
        return true
    }
}