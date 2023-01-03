package ru.barinov.obdroid.ui.connectionsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.databinding.BtItemBinding
import ru.barinov.obdroid.databinding.WifiItemLayoutBinding
import ru.barinov.obdroid.ui.utils.ConnectionsDiff

class ConnectionsAdapter : RecyclerView.Adapter<BaseViewHolder<ConnectionItem>>() {

    private var items = listOf<ConnectionItem>()

    private var onItemClick: ConnectionClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ConnectionItem> {
        return if (viewType == 0) {
            WifiViewHolder(WifiItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            BTViewHolder(BtItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            ConnectionItem.ConnectionType.WIFI -> 0
            ConnectionItem.ConnectionType.BLUETOOTH -> 1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ConnectionItem>, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
        onItemClick?.let { listener ->
            holder.itemView.setOnClickListener {
                listener.onItemClick(item, holder.itemView)
            }
        }

    }

    override fun getItemCount(): Int = items.size

    private fun getItem(position: Int): ConnectionItem = items[position]

    fun newItems(new: List<ConnectionItem>) {
        val result = DiffUtil.calculateDiff(ConnectionsDiff(items, new))
        items = new
        result.dispatchUpdatesTo(this)
    }

    fun addItemClickListener(listener: ConnectionClickListener) {
        onItemClick = listener
    }

    interface ConnectionClickListener {

        fun onItemClick(item: ConnectionItem, itemView : View)
    }
}