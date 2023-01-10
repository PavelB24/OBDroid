package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.barinov.obdroid.ui.uiModels.PidCommand

@TypeConverters(
    CommandEntity.CommandCategoryTypeConverter::class,
    MeasurementUnitTypeConverter::class
)
@Entity(
    tableName = "pid_commands",
    primaryKeys = ["hex_value", "command_section_hex"]
)
data class CommandEntity(
    @ColumnInfo(name = "hex_value")
    val hexValue: String,
    @ColumnInfo(name = "octal_value")
    val octalValue: Int,
    @ColumnInfo(name = "command_section_hex")
    val commandSectionHex: String,
    val category: CommandCategory,
    @ColumnInfo(name = "command_desc_eng")
    val commandDescEng: String,
    @ColumnInfo(name = "command_desc_rus")
    val commandDescRus: String?,
    @ColumnInfo(name = "is_fav")
    val isFav: Boolean,
    @ColumnInfo(name = "is_custom_command")
    val isCustomCommand: Boolean,
    @ColumnInfo(name = "measurement_unit")
    val measurementUnit: MeasurementUnit?,
    val available: Boolean,
    @ColumnInfo(name = "is_dynamic")
    val isDynamic: Boolean
){

    class CommandCategoryTypeConverter {
        @TypeConverter
        fun toType(ordinal: Int) = CommandCategory.values()[ordinal]

        @TypeConverter
        fun fromType(type: CommandCategory) = type.ordinal
    }

}

fun CommandEntity.toPidCommand(): PidCommand{
    return PidCommand(
        hexValue,
        octalValue,
        commandSectionHex,
        category,
        commandDescEng,
        commandDescRus,
        isFav,
        isCustomCommand,
        measurementUnit
    )
}