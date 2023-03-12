package ru.barinov.obdroid.ui.sensorsFragment

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.barinov.obdroid.base.SearchHandler
import ru.barinov.obdroid.domain.CommandCategory

interface SensorsToolbarActionsHandler: SearchHandler {


    fun getSavedFavsState(): Boolean

    fun getSavedSortState(): Int

    fun onCategoryChanged(category: CommandCategory)

    fun onFavButtonClicked(showFavs: Boolean)
}