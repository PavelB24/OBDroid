package ru.barinov.obdroid.ui.connectionsFragment

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.BtItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem

class ConnectionsListHandler(private val scope: CoroutineScope) {


    val scanResult = MutableSharedFlow<List<ConnectionItem>>(1, 10)

    private var lastList: List<ConnectionItem>? = null

    fun addBt(btDevice: BtConnectionItem) {
        if (lastList == null) {
            lastList = listOf(btDevice)
            lastList?.let {
                scope.launch {
                    scanResult.emit(it)
                }
            }
        } else {
            val resultList = mutableListOf<ConnectionItem>()
            lastList?.toMutableList()?.let { list ->
                list.removeIf { it is BtConnectionItem && it.address == btDevice.address }
                resultList.addAll(list)
                resultList.add(0, btDevice)
            }
            lastList = resultList
            scope.launch {
                scanResult.emit(resultList)
            }
        }
    }

    fun addWiFi(wifiDevices: List<WifiConnectionItem>) {
        if (lastList == null) {
            lastList = wifiDevices
            lastList?.let {
                scope.launch {
                    scanResult.emit(it)
                }
            }
            return
        }
        val resultList = mutableListOf<ConnectionItem>()
        lastList?.toMutableList()?.let { list ->
            list.removeIf { it is WifiConnectionItem }
            resultList.addAll(list)
            resultList.addAll(0, wifiDevices)
        }
        lastList = resultList
        scope.launch {
            scanResult.emit(resultList)
        }
    }

    fun getList() = lastList

    fun clearResults() {
        lastList = null
    }

    fun removeItem(item: WifiConnectionItem) {
        lastList?.let {
            scope.launch {
                val changedList = it.toMutableList().also { it.remove(item) }
                lastList = changedList
                scanResult.emit(changedList)
            }
        }
    }

    fun removeItem(item: BtItem) {
        lastList?.let {
            scope.launch {
                val changedList = it.toMutableList().also { it.remove(item) }
                lastList = changedList
                scanResult.emit(changedList)
            }
        }
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