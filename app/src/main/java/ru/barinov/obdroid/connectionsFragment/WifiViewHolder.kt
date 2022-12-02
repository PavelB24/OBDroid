package ru.barinov.obdroid.connectionsFragment

import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.WifiItemLayoutBinding
import ru.barinov.obdroid.uiModels.WifiConnectionItem

class WifiViewHolder(
    private val binding: WifiItemLayoutBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem) {
        if (item !is WifiConnectionItem){
            throw IllegalAccessException()
        }
    }
}