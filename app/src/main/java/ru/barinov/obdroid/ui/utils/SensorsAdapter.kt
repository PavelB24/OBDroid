package ru.barinov.obdroid.ui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.databinding.CommandItemLayoutBinding
import ru.barinov.obdroid.domain.CommandActions
import ru.barinov.obdroid.ui.sensorsFragment.CommandsDiffUtil

class SensorsAdapter : RecyclerView.Adapter<CommandViewHolder>() {

    private var commandList: List<Command> = listOf()
    private var action: CommandActions? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandViewHolder {
        return CommandViewHolder(
            CommandItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: CommandViewHolder, position: Int) {
        val item = getItem(holder.layoutPosition)
        holder.onBind(item)
        holder.itemView.apply {
            setOnLongClickListener {
                action?.onLongClick(item, holder.itemView)
                true
            }
            setOnClickListener {
                action?.onClick(item)
            }
        }
    }

    fun updateList(commands: List<Command>){
        val result = DiffUtil.calculateDiff(CommandsDiffUtil(commandList, commands))
        commandList = commands
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = commandList.size

    private fun getItem(position: Int) = commandList[position]

    fun bindActions(actions: CommandActions) {
        this.action = actions
    }
}