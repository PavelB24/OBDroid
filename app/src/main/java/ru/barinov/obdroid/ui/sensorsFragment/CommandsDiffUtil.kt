package ru.barinov.obdroid.ui.sensorsFragment

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.obdroid.base.Command

class CommandsDiffUtil(
    private val oldList: List<Command>,
    private val newList: List<Command>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].command == newList[newItemPosition].command
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].command == newList[newItemPosition].command &&
         oldList[oldItemPosition].category == newList[newItemPosition].category &&
         oldList[oldItemPosition].measurementUnit == newList[newItemPosition].measurementUnit
    }
}