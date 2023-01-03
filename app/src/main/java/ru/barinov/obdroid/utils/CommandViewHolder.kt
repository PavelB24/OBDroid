package ru.barinov.obdroid.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.databinding.CommandItemLayoutBinding
import ru.barinov.obdroid.databinding.WifiItemLayoutBinding
import ru.barinov.obdroid.ui.uiModels.PidCommand

class CommandViewHolder(
    private val binding: CommandItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Command) {
        when(item){
            is PidCommand -> {fillWithPid(item)}
        }
    }

    private fun fillWithPid(item: PidCommand) {
        binding.title.text = item.commandDescEng
    }
}