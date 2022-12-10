package ru.barinov.obdroid.ui.connectionsFragment

import androidx.appcompat.content.res.AppCompatResources
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.databinding.WifiItemLayoutBinding
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem

class WifiViewHolder(
    private val binding: WifiItemLayoutBinding
) : BaseViewHolder<ConnectionItem>(binding.root) {

    override fun onBind(item: ConnectionItem) {
        if (item !is WifiConnectionItem){
            throw IllegalAccessException()
        }
        binding.apply {
            wfBssid.text = item.bssid
            wfSsid.text = item.ssid
            wfParams.text = "Fr: ${item.frequency} Ch:${item.channel}"
            connectionIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context, R.drawable.wifi_logo
                )
            )
        }
    }
}