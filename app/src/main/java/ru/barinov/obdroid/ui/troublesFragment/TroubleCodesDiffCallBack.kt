package ru.barinov.obdroid.ui.troublesFragment

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroubleCodesDiffCallBack: DiffUtil.ItemCallback<TroubleCode>() {

    override fun areItemsTheSame(oldItem: TroubleCode, newItem: TroubleCode): Boolean {
       return oldItem.code == newItem.code && oldItem.detectionDate == newItem.detectionDate
    }

    override fun areContentsTheSame(oldItem: TroubleCode, newItem: TroubleCode): Boolean {
        return oldItem == newItem
    }
}