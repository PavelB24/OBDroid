package ru.barinov.obdroid.ui.troublesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.ItemInteractor
import ru.barinov.obdroid.databinding.TroubleItemLayoutBinding
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroublesPagingAdapter: PagingDataAdapter<TroubleCode, BaseViewHolder<TroubleCode>>(TroubleCodesDiffCallBack()) {

    private var interactor: ItemInteractor<TroubleCode>? = null

    override fun onBindViewHolder(holder: BaseViewHolder<TroubleCode>, position: Int) {

        getItem(position)?.let {item->
            holder.onBind(item)
            holder.itemView.setOnClickListener {
                interactor?.onClick(item)
            }
            holder.itemView.setOnLongClickListener {
                interactor?.onLongClick(item, it)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TroubleCode> {
        return TroubleViewHolder(
            TroubleItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun bindActions(interactor: ItemInteractor<TroubleCode>){
        this.interactor = interactor
    }
}