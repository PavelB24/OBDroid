package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters(CommandEntity.CommandCategoryTypeConverter::class)
@Entity(tableName = "pid_commands")
data class CommandEntity(
    @PrimaryKey
    @ColumnInfo(name = "dex_value")
    val dexValue: String,
    @ColumnInfo(name = "dec_value")
    val decValue: Int,
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
    val isCustomCommand: Boolean
){


    object CommandCategoryTypeConverter{
        @TypeConverter
        fun toType(ordinal: Int) = CommandCategory.values()[ordinal]
        @TypeConverter
        fun fromType(type: CommandCategory) = type.ordinal

    }
}