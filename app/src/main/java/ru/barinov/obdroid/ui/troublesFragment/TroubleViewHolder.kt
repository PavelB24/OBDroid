package ru.barinov.obdroid.ui.troublesFragment

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.BaseViewHolder
import ru.barinov.obdroid.databinding.TroubleItemLayoutBinding
import ru.barinov.obdroid.domain.TroubleCodeType
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroubleViewHolder(
    private val binding: TroubleItemLayoutBinding
) : BaseViewHolder<TroubleCode>(binding.root) {


    override fun onBind(item: TroubleCode?) {
        item?.let {
            binding.troubleCode.text = it.code
            binding.troubleTypeIcon.setImageDrawable(getDrawableByType(it.type))
        }
    }

    private fun getDrawableByType(type: TroubleCodeType): Drawable? {
        val drawableId = when(type){
            TroubleCodeType.UNDEFINED -> R.drawable.check_eng
            TroubleCodeType.FUEL_AND_AIR -> R.drawable.fuel_injector
            TroubleCodeType.IGNITION_SYSTEM -> R.drawable.common_eng_spark
            TroubleCodeType.EMISSION_CONTROLS -> R.drawable.oxygen_sensor
            TroubleCodeType.SPEED_CONTROL_AUXILIARY -> R.drawable.parameter
            TroubleCodeType.ECU_AND_AUXILIARY -> R.drawable.electrical
            TroubleCodeType.TRANSMISSION -> R.drawable.chassis
            TroubleCodeType.GM -> R.drawable.check_eng
            TroubleCodeType.CHASSIS -> R.drawable.chassis
            TroubleCodeType.COMMON -> R.drawable.check_eng
            TroubleCodeType.HEADER -> throw IllegalStateException()
        }
        return ContextCompat.getDrawable(itemView.context, drawableId)
    }
}