package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(
    CommandEntity.CommandCategoryTypeConverter::class,
    MeasurementUnitTypeConverter::class
)
@Entity(tableName = "at_commands")
data class AtCommandEntity(
    @PrimaryKey
    val command: String,
    @ColumnInfo(name = "command_desc_eng")
    val commandDescEng: String,
    @ColumnInfo(name = "command_desc_rus")
    val commandDescRus: String?,
    @ColumnInfo(name = "measurement_unit")
    val measurementUnit: MeasurementUnit?,
    @ColumnInfo(name = "accessibility")
    val accessAbleForUser: Boolean,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    val category: CommandCategory,
    @ColumnInfo(name = "is_dynamic")
    val isDynamic: Boolean
)