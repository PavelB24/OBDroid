package ru.barinov.obdroid.uiModels

import ru.barinov.obdroid.base.ConnectionItem

data class BtConnectionItem(
    override val type : ConnectionType,
    val address : String
    ) : ConnectionItem(type)