package ru.barinov.obdroid.ui.uiModels

import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit

data class PidCommand(
    val hexValue: String,
    val octalValue: Int,
    val commandSectionHex: String,
    override val category: CommandCategory,
    val commandDescEng: String,
    val commandDescRus: String?,
    val isFavorite: Boolean,
    val isCustomCommand: Boolean,
    override val measurementUnit: MeasurementUnit?
): Command(hexValue, category, measurementUnit, true)