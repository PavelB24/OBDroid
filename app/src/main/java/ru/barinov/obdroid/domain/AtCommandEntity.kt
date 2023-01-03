package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "at_commands")
@TypeConverters(MeasurementUnitTypeConverter::class)
data class AtCommandEntity(
    @PrimaryKey
    val command: String,
    @ColumnInfo(name = "command_desc_eng")
    val commandDescEng: String,
    @ColumnInfo(name = "command_desc_rus")
    val commandDescRus: String,
    @ColumnInfo(name = "measurement_unit")
    val measurementUnit: MeasurementUnit,
    @ColumnInfo(name = "accessibility")
    val accessAbleForUser: Boolean,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)