package ru.barinov.obdroid.ui.profilesListFragment

import androidx.recyclerview.widget.DiffUtil
import ru.barinov.obdroid.ui.uiModels.Profile

class ProfileDiffUtil(
    private val oldList: List<Profile>,
    private val newList: List<Profile>
): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}