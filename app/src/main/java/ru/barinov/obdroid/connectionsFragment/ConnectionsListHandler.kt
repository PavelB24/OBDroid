package ru.barinov.obdroid.connectionsFragment

import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

object ConnectionsListHandler {

    private var lastList: List<ConnectionItem>? = null

    fun addBt(btDevices: List<BtConnectionItem>): List<ConnectionItem> {
        lastList ?: return btDevices
        val resultList = mutableListOf<ConnectionItem>()
        lastList?.let {
            resultList.addAll(it)
        }
        btDevices.forEach { new ->
            resultList.removeIf { stored ->
                if (stored is BtConnectionItem) {
                    !btDevices.toMutableList()
                        .map { device ->
                            device.address
                        }.contains(stored.address) ||
                            new.address == stored.address

                } else false
            }
        }
        resultList.addAll(btDevices)
        lastList = resultList
        return resultList
    }

    fun addWiFi(wifiDevices: List<WifiConnectionItem>): List<ConnectionItem> {
        lastList ?: return wifiDevices
        val resultList = mutableListOf<ConnectionItem>()
        lastList?.let {
            resultList.addAll(it)
        }
        wifiDevices.forEach { new ->
            resultList.removeIf { stored ->
                if (stored is WifiConnectionItem) {
                    !wifiDevices.toMutableList()
                        .map { device ->
                            device.bssid
                        }.contains(stored.bssid) ||
                            new.bssid == stored.bssid

                } else false
            }
        }
        resultList.addAll(wifiDevices)
        lastList = resultList
        return resultList
    }

//    fun handleList(input : List<ConnectionItem>) : List<ConnectionItem>{
//        lastList ?: return input
//        val resultList = mutableListOf<ConnectionItem>()
//        lastList?.let {
//            resultList.addAll(it)
//        }
//        input.forEach { new->
//            resultList.removeIf {
//                when{
//                    new is BtConnectionItem && it is BtConnectionItem ->{
//                        new.address == it.address
//                    }
//                    new is WifiConnectionItem && it is WifiConnectionItem ->{
//                        new.bssid == it.bssid
//                    }
//
//                    else -> false
//                }
//            }
//        }
//    }
}