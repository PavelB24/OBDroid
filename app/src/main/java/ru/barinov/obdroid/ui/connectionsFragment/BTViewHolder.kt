package ru.barinov.obdroid.ui.connectionsFragment

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.BtItemBinding
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem

class BTViewHolder(
    private val binding: BtItemBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem?) {
        if (item !is BtConnectionItem) {
            throw IllegalAccessException()
        }
        binding.apply {
            btAddress.text = item.address

            btName.text = if (item.name.isNullOrEmpty())
                itemView.context.getString(R.string.unknown_bt_name)
            else item.name

            setIcon(item.btClass, binding.connectionIcon)
            setRssi(item.rssi, binding.rssiField)

            if (item.boundState == BluetoothDevice.BOND_BONDED) {
                btBoundIcon.visibility = View.VISIBLE
                btBoundIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemView.context, R.drawable.bound_logo
                    )
                )
            } else {
                btBoundIcon.setImageDrawable(null)
                btBoundIcon.visibility = View.GONE
            }

        }
    }

    private fun setRssi(rssi: Short?, rssiField: TextView) {
        if (rssi != null) {
            val context = itemView.context
            when (rssi) {
                in -50..0 -> {
                    rssiField.apply {
                        text = context.getString(R.string.good_bt_rssi_signal)
                        rssiField.setCompoundDrawablesWithIntrinsicBounds(
                            AppCompatResources.getDrawable(context, R.drawable.high_signal),
                            null,
                            null,
                            null
                        )
                    }
                }
                in -70..-50 -> {
                    rssiField.apply {
                        text = context.getString(R.string.mid_bt_rssi_signal)
                        rssiField.setCompoundDrawablesWithIntrinsicBounds(
                            AppCompatResources.getDrawable(context, R.drawable.mid_signal),
                            null,
                            null,
                            null
                        )
                    }
                }
                in -100..-70 -> {
                    rssiField.apply {
                        text = context.getString(R.string.bad_bt_rssi_signal)
                        rssiField.setCompoundDrawablesWithIntrinsicBounds(
                            AppCompatResources.getDrawable(context, R.drawable.low_signal),
                            null,
                            null,
                            null
                        )
                    }
                }
                else -> rssiField.visibility = View.GONE
            }
            rssiField.visibility = View.VISIBLE
        } else rssiField.visibility = View.GONE
    }

    private fun setIcon(clazz: BluetoothClass?, connectionIcon: ImageView) {
        if (clazz != null) {
            when{
                clazz.deviceClass == BluetoothClass.Device.Major.UNCATEGORIZED ->{
                    connectionIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context, R.drawable.bt_logo
                        )
                    )
                }
                clazz.majorDeviceClass == BluetoothClass.Device.Major.COMPUTER ->{
                    connectionIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context, R.drawable.baseline_laptop_24
                        )
                    )
                }
                else -> {
                    connectionIcon.setImageDrawable(
                        AppCompatResources.getDrawable(
                            itemView.context, R.drawable.bt_logo
                        )
                    )
                }
            }
        } else {
            connectionIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context, R.drawable.bt_logo
                )
            )
        }

    }
}