package ru.barinov.obdroid.uiModels

import ru.barinov.obdroid.base.ConnectionItem

data class WifiConnectionItem(
    override val type : ConnectionType,
    val bssid : String
    ) : ConnectionItem(type)