package ru.barinov.obdroid.ui.utils

import android.util.Log
import androidx.core.content.ContextCompat
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.databinding.CommandItemLayoutBinding
import ru.barinov.obdroid.domain.AtCommand
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.ui.uiModels.PidCommand
import java.util.Locale

class CommandViewHolder(
    private val binding: CommandItemLayoutBinding
) : BaseViewHolder<Command>(binding.root) {

    override fun onBind(item: Command) {
        when (item) {
            is PidCommand -> {
                fillWithPid(item)
            }
            is AtCommand -> {
                fillWithAt(item)
            }
        }
    }

    private fun fillWithAt(item: AtCommand) {
        binding.title.text =
            if (Locale.getDefault().language == "ru") item.commandDescRus else item.commandDescEng
        val imgId = selectIcon(item.category)
        Log.d("@@@", "item fav == ${item.isFavorite}")
        checkFavorite(item.isFavorite)
        displayIcon(imgId)
    }

    private fun displayIcon(imgId: Int) {
        if (imgId != -1) {
            binding.commandIcon.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, imgId)
            )
        } else
            binding.commandIcon.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.obd_stub)
            )
    }

    private fun checkFavorite(favorite: Boolean) {
        if (favorite) {
            binding.isFavIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.fav_icon
                )
            )
        } else binding.isFavIcon.setImageDrawable(null)
    }


    private fun fillWithPid(item: PidCommand) {
        binding.title.text =
            if (Locale.getDefault().language == "ru") item.commandDescRus else item.commandDescEng
        val imgId = selectIcon(item.category)
        checkFavorite(item.isFavorite)
        displayIcon(imgId)
    }

    private fun selectIcon(category: CommandCategory): Int {
        return when (category) {
            CommandCategory.DIESEL -> R.drawable.diesel
            CommandCategory.UNDEFINED -> R.drawable.obd_stub
            CommandCategory.ELECTRICAL -> R.drawable.electro_acc
            CommandCategory.OXYGEN_SENSOR -> R.drawable.oxygen_sensor
            CommandCategory.VEHICLE_INFO -> R.drawable.vehicle
            CommandCategory.IGNITION -> R.drawable.common_eng_spark
            CommandCategory.AIR_FLOW -> R.drawable.air_flow
            CommandCategory.FUEL_INJECTION -> R.drawable.fuel_injector
            CommandCategory.PARAMETER -> R.drawable.parameter
            CommandCategory.NUMBER_VAL -> R.drawable.numbers
            CommandCategory.TEMPERATURE -> R.drawable.thermometer
            CommandCategory.THROTTLE -> R.drawable.throttle
            CommandCategory.SWITCH -> R.drawable.two_state_switch
            CommandCategory.TURBO_CHARGER -> R.drawable.turbo
            else -> {
                -1
            }

        }
    }
}