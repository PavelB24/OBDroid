package ru.barinov.obdroid.uiModels

import ru.barinov.obdroid.base.ConnectionItem

data class WifiConnectionItem(
    override val type : ConnectionType,
    val bssid : String,
    val frequency : Int,
    val timeStamp : Long,
    val channel : Int,
    val ssid : String
    ) : ConnectionItem(type)