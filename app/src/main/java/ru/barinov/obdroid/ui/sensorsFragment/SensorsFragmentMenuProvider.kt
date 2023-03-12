package ru.barinov.obdroid.ui.sensorsFragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.get
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.*
import ru.barinov.obdroid.domain.CommandCategory

class SensorsFragmentMenuProvider(
    private val toolbarActionHandler: SensorsToolbarActionsHandler,
): ConnectionStateItemHolder(), ToolbarSearcher {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.initSearch(menu, menuInflater, SearchHelper((toolbarActionHandler)))
        super.inflateAndManageAnimation(menu, menuInflater, R.menu.sensors_toolbar_menu)
        menu.getItem(1).isChecked = toolbarActionHandler.getSavedFavsState()
        val savedSortItem = when (toolbarActionHandler.getSavedSortState()) {
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
        toolbarActionHandler.apply {
            when (menuItem.itemId) {
                R.id.cat_all -> onCategoryChanged(CommandCategory.UNDEFINED)
                R.id.cat_diesel -> onCategoryChanged(CommandCategory.DIESEL)
                R.id.cat_electric -> onCategoryChanged(CommandCategory.ELECTRICAL)
                R.id.cat_throttle -> onCategoryChanged(CommandCategory.THROTTLE)
                R.id.cat_air -> onCategoryChanged(CommandCategory.AIR_FLOW)
                R.id.cat_fuel_injection -> onCategoryChanged(CommandCategory.FUEL_INJECTION)
                R.id.cat_temperature -> onCategoryChanged(CommandCategory.TEMPERATURE)
                R.id.cat_oxygen -> onCategoryChanged(CommandCategory.OXYGEN_SENSOR)
                R.id.cat_info -> onCategoryChanged(CommandCategory.VEHICLE_INFO)
                R.id.cat_turbo -> onCategoryChanged(CommandCategory.TURBO_CHARGER)
                R.id.fav_toolbar_button -> onFavButtonClicked(menuItem.isChecked)
            }
        }
        return true
    }
}