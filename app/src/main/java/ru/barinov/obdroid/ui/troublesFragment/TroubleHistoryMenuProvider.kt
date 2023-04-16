package ru.barinov.obdroid.ui.troublesFragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.SearchHelper
import ru.barinov.obdroid.base.ToolbarSearcher
import ru.barinov.obdroid.domain.TroubleCodeType

class TroubleHistoryMenuProvider(
    private val toolbarActionHandler: TroubleHistoryToolbarActionsHandler
):  ToolbarSearcher {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.initSearch(menu, menuInflater , SearchHelper(toolbarActionHandler))
        menuInflater.inflate(R.menu.troubles_history_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.history_type_item -> {
                menuItem.isChecked = !menuItem.isChecked
                toolbarActionHandler.onModeChanged(
                    if(menuItem.isChecked) TroublePageType.ALL_KNOWN
                    else TroublePageType.DETECTED
                )
            }
            R.id.all_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.UNDEFINED)
            R.id.fuel_and_air_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.FUEL_AND_AIR)
            R.id.ignition_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.IGNITION_SYSTEM)
            R.id.emission_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.EMISSION_CONTROLS)
            R.id.ecu_and_aux_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.ECU_AND_AUXILIARY)
            R.id.transmission_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.TRANSMISSION)
            R.id.chassis_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.CHASSIS)
            R.id.common_troubles -> toolbarActionHandler.onTroubleTypeChanged(TroubleCodeType.COMMON)
            else -> {return false}
        }
        return true
    }
}