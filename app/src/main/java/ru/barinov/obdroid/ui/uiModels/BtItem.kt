package ru.barinov.obdroid.ui.uiModels

import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.ui.connectionsFragment.BtConnectionI

data class BtItem(
    override val type : ConnectionType,
    val address : String,
    val name : String?,
    val boundState : Int
): ConnectionItem(type)

