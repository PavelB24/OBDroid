package ru.barinov.obdroid.utils

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem
import ru.barinov.obdroid.base.ConnectionItem

class ConnectionsDiff(
    private val oldList : List<ConnectionItem>,
    private val newList : List<ConnectionItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return when{
            old is BtConnectionItem && new is BtConnectionItem -> {
                old.address == new.address
            }
            old is WifiConnectionItem && new is WifiConnectionItem -> {
                old.bssid == new.bssid
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        TODO("Not yet implemented")
    }
}