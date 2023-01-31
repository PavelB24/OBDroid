package ru.barinov.obdroid.data

import androidx.room.ColumnInfo
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit

data class CommonCommandEntity(
    val command: String,
    val octalValue: Int,
    val commandSectionHex: String,
    val category: CommandCategory,
    val commandDescEng: String,
    val commandDescRus: String?,
    val isFav: Boolean,
    val isCustomCommand: Boolean,
    val measurementUnit: MeasurementUnit?,
    val available: Boolean,
    val isDynamic: Boolean,
    val accessAbleForUser: Boolean
)
