package ru.barinov.obdroid.ui.troublesFragment

import ru.barinov.obdroid.base.SearchHandler
import ru.barinov.obdroid.domain.TroubleCodeType

interface TroubleHistoryToolbarActionsHandler: SearchHandler {

    fun onTroubleTypeChanged(type: TroubleCodeType)

    fun onModeChanged(type: TroublePageType)
}