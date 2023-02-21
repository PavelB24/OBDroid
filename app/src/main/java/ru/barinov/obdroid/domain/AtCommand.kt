package ru.barinov.obdroid.domain

import ru.barinov.obdroid.base.Command


data class AtCommand(
    override val command: String,
    val commandDescEng: String,
    val commandDescRus: String?,
    override val measurementUnit: MeasurementUnit?,
    val accessAbleForUser: Boolean,
    val isFavorite: Boolean,
    override val category: CommandCategory,
    val isDynamic: Boolean
): Command(command, category, measurementUnit, false)
