package ru.barinov.obdroid.base

abstract class ConnectionItem(open val type : ConnectionType) {

    enum class ConnectionType{
        WIFI, BLUETOOTH
    }
}