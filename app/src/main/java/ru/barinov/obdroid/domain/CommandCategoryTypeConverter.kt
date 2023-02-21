package ru.barinov.obdroid.domain

import androidx.room.TypeConverter

class CommandCategoryTypeConverter {

        @TypeConverter
        fun toType(ordinal: Int) = CommandCategory.values()[ordinal]

        @TypeConverter
        fun fromType(type: CommandCategory) = type.ordinal
    }