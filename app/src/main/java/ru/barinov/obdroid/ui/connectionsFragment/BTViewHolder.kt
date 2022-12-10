package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothDevice
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.BtItemBinding
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

class BTViewHolder(
    private val binding: BtItemBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem) {
        if (item !is BtConnectionItem) {
            throw IllegalAccessException()
        }
        binding.apply {
            btAddress.text = item.address
            if (item.name.isNullOrEmpty() || item.name.isBlank()) {
                btName.text = itemView.context.getString(R.string.unknown_bt_name)
            }
            connectionIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context, R.drawable.bt_logo
                )
            )
            if (item.boundState == BluetoothDevice.BOND_BONDED) {
                btBoundIcon.visibility = View.VISIBLE
                btBoundIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemView.context, R.drawable.bound_logo
                    )
                )
            }

        }
    }

}