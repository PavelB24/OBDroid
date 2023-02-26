package ru.barinov.obdroid.ui.profilesListFragment

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.databinding.ProfileItemLayoutBinding
import ru.barinov.obdroid.ui.uiModels.Profile

class ProfileViewHolder(
    private val binding: ProfileItemLayoutBinding,
    private val showProtoSelector: Boolean = false,
    private val directProtoPicker: ((Int) -> (Unit))? = null
) : BaseViewHolder<Profile>(binding.root) {

    override fun onBind(item: Profile?) {
        item?.let {
            binding.protocolList.visibility = if (showProtoSelector) View.VISIBLE else View.GONE
            binding.profileName.text = it.name
            if (showProtoSelector) {
                binding.protocolList.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        directProtoPicker?.invoke(position)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }
}