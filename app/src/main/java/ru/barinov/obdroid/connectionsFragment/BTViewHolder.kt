package ru.barinov.obdroid.connectionsFragment

import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.BtItemBinding
import ru.barinov.obdroid.uiModels.BtConnectionItem

class BTViewHolder(
    private val binding: BtItemBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem) {
        if (item !is BtConnectionItem){
            throw IllegalAccessException()
        }
        binding.apply {
            btAddress.text = item.address
            if(item.name.isNullOrEmpty()){
                btName.text = itemView.context.getString(R.string.unknown_bt_name)
            }


        }
    }

}