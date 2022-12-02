package ru.barinov.obdroid.connectionsFragment

import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.BtItemLayoutBinding
import ru.barinov.obdroid.uiModels.BtConnectionItem

class BTViewHolder(
    private val binding: BtItemLayoutBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem) {
        if (item !is BtConnectionItem){
            throw IllegalAccessException()
        }
    }

}