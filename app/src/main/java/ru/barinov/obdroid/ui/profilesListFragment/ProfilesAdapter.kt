package ru.barinov.obdroid.ui.profilesListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.obdroid.base.ItemInteractor
import ru.barinov.obdroid.databinding.ProfileItemLayoutBinding
import ru.barinov.obdroid.ui.uiModels.Profile

class ProfilesAdapter : RecyclerView.Adapter<ProfileViewHolder>() {

    private var list = listOf<Profile>()
    private var pickAction: ItemInteractor<Profile>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            ProfileItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = getItem(holder.layoutPosition)
        holder.onBind(profile)
        holder.itemView.setOnClickListener {
            pickAction?.onClick(profile)
        }
        holder.itemView.setOnLongClickListener {
            pickAction?.onLongClick(profile, it)
            pickAction != null
        }
    }

    override fun getItemCount() = list.size

    private fun getItem(position: Int) = list[position]

    fun bindPickAction(action: ItemInteractor<Profile>) {
        pickAction = action
    }

    fun updateList(newList: List<Profile>) {
        val result = DiffUtil.calculateDiff(ProfileDiffUtil(list, newList))
        list = newList
        result.dispatchUpdatesTo(this)
    }
}