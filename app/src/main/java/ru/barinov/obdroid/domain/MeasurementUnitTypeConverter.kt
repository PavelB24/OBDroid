package ru.barinov.obdroid.domain

import androidx.room.TypeConverter

class MeasurementUnitTypeConverter{
    @TypeConverter
    fun toType(ordinal: Int?) =
        if(ordinal != null) MeasurementUnit.values()[ordinal] else null
    @TypeConverter
    fun fromType(type: MeasurementUnit?) = type?.ordinal
}