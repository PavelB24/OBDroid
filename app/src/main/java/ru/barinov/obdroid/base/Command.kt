package ru.barinov.obdroid.base

import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit

abstract class Command(
    val command: String,
    open val category: CommandCategory,
    open val measurementUnit: MeasurementUnit?,
    val isPid: Boolean
)