package ru.barinov.obdroid.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "trouble_codes")
@TypeConverters(TroubleCodeEntity.TroubleCodeTypeConverter::class)
data class TroubleCodeEntity(
    @PrimaryKey
    val code: String,
    val description: String,
    val translate: String?,
    val type: TroubleCodeType
){



   object TroubleCodeTypeConverter {
       @TypeConverter
       fun toType(ordinal: Int) = TroubleCodeType.values()[ordinal]
       @TypeConverter
       fun fromType(type: TroubleCodeType) = type.ordinal
   }
}