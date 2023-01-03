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
    primaryKeys = ["dex_value", "command_section_dex"]
)
data class CommandEntity(
    @ColumnInfo(name = "dex_value")
    val dexValue: String,
    @ColumnInfo(name = "octal_value")
    val octalValue: Int,
    @ColumnInfo(name = "command_section_dex")
    val commandSectionDex: String,
    val category: CommandCategory,
    @ColumnInfo(name = "command_desc_eng")
    val commandDescEng: String,
    @ColumnInfo(name = "command_desc_rus")
    val commandDescRus: String?,
    @ColumnInfo(name = "is_chosen")
    val isChosen: Boolean,
    @ColumnInfo(name = "is_custom_command")
    val isCustomCommand: Boolean,
    @ColumnInfo(name = "measurement_unit")
    val measurementUnit: MeasurementUnit?
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
        dexValue,
        octalValue,
        commandSectionDex,
        category,
        commandDescEng,
        commandDescRus,
        isChosen,
        isCustomCommand,
        measurementUnit
    )
}