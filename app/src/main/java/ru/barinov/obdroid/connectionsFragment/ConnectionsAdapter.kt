package ru.barinov.obdroid.connectionsFragment

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.base.BaseViewHolder

class ConnectionsAdapter : RecyclerView.Adapter<BaseViewHolder<ConnectionItem>>() {

    private var items = listOf<ConnectionItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ConnectionItem> {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ConnectionItem>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun newItems(new : List<ConnectionItem>){
        Log.d("@@@", new.toString())
    }
}