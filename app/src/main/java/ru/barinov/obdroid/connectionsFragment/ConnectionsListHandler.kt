package ru.barinov.obdroid.connectionsFragment

import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem

object ConnectionsListHandler {

    private var lastList: List<ConnectionItem>? = null

    fun addBt(btDevice : BtConnectionItem): List<ConnectionItem> {
        if(lastList == null){
            lastList = listOf(btDevice)
            return lastList!!
        }
        val resultList = mutableListOf<ConnectionItem>()
        lastList?.toMutableList()?.let { list->
            list.removeIf { it is BtConnectionItem && it.address == btDevice.address }
            resultList.addAll(list)
            resultList.add(btDevice)
        }
        lastList = resultList
        return resultList
    }

    fun addWiFi(wifiDevices: List<WifiConnectionItem>): List<ConnectionItem> {
        if(lastList == null){
            lastList = wifiDevices
            return lastList!!
        }
        val resultList = mutableListOf<ConnectionItem>()
        lastList?.toMutableList()?.let { list->
            list.removeIf { it is WifiConnectionItem }
            resultList.addAll(list)
            resultList.addAll(wifiDevices)
        }
        lastList = resultList
        return resultList
    }

    fun clearResults(){
        lastList = null
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