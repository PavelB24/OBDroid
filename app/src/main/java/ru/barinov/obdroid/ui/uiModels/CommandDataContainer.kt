package ru.barinov.obdroid.ui.uiModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit

@Parcelize
data class CommandDataContainer(
    val command: String,
    val sectionHex: String?,
    val isDynamic: Boolean,
    val measurementUnit: MeasurementUnit,
    val commandCategory: CommandCategory
): Parcelable
