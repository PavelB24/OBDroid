package ru.barinov.obdroid.ui.troublesFragment

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroublesPagingAdapter: PagingDataAdapter<TroubleCode, BaseViewHolder<TroubleCode>>(TroubleCodesDiffCallBack()) {


    override fun onBindViewHolder(holder: BaseViewHolder<TroubleCode>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TroubleCode> {
        TODO("Not yet implemented")
    }
}