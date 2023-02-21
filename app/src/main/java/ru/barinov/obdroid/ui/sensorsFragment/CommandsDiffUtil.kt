package ru.barinov.obdroid.ui.sensorsFragment

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.domain.AtCommand
import ru.barinov.obdroid.ui.uiModels.PidCommand


//todo refactor
class CommandsDiffUtil(
    private val oldList: List<Command>,
    private val newList: List<Command>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return when{
            old.isPid && new.isPid -> {
                (old as PidCommand).command == (new as PidCommand).command
            }
            !old.isPid && !new.isPid -> {
                (old as AtCommand).command == (new as AtCommand).command
            }
            else -> true
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return if(old.isPid && new.isPid){
            old as PidCommand == new as PidCommand
        } else if(!new.isPid && !old.isPid){
            old as AtCommand == new as AtCommand
        }else false
    }
}