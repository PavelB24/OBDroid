package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.ui.uiModels.PidCommand

@TypeConverters(
    CommandCategoryTypeConverter::class,
    MeasurementUnitTypeConverter::class
)
data class CommandEntity(
    @ColumnInfo(name = "command_body")
    val commandBody: String,
    @ColumnInfo(name = "octal_value")
    val octalValue: Int?,
    @ColumnInfo(name = "command_section_hex")
    val commandSectionHex: String?,
    val category: CommandCategory,
    @ColumnInfo(name = "command_desc_eng", typeAffinity = ColumnInfo.TEXT)
    val commandDescEng: String,
    @ColumnInfo(name = "command_desc_rus", typeAffinity = ColumnInfo.TEXT)
    val commandDescRus: String?,
    @ColumnInfo(name = "is_favorite")
    val isFav: Boolean,
    @ColumnInfo(name = "is_custom_command")
    val isCustomCommand: Boolean,
    @ColumnInfo(name = "measurement_unit")
    val measurementUnit: MeasurementUnit?,
    @ColumnInfo(name = "accessibility")
    val available: Boolean,
    @ColumnInfo(name = "is_dynamic")
    val isDynamic: Boolean,
    @ColumnInfo(name = "is_pid")
    val isPid: Boolean
){

    fun toHandledCommand(): Command{
        return if(isPid){
            PidCommand(
                commandBody,
                octalValue!!,
                commandSectionHex!!,
                category,
                commandDescEng,
                commandDescRus,
                isFav,
                isCustomCommand,
                measurementUnit
            )
        } else {
            AtCommand(
                commandBody,
                commandDescEng,
                commandDescRus,
                measurementUnit,
                available,
                isFav,
                category,
                isDynamic
            )
        }
    }
}