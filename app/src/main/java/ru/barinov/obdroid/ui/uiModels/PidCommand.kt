package ru.barinov.obdroid.ui.uiModels

import androidx.room.ColumnInfo
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit

data class PidCommand(
    val dexValue: String,
    val octalValue: Int,
    val commandSectionDex: String,
    override val category: CommandCategory,
    val commandDescEng: String,
    val commandDescRus: String?,
    val isChosen: Boolean,
    val isCustomCommand: Boolean,
    override val measurementUnit: MeasurementUnit?
): Command(dexValue, category, measurementUnit, true)