package ru.barinov.obdroid.domain

import androidx.room.*
import ru.barinov.obdroid.ui.uiModels.TroubleCode
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "trouble_codes",
    primaryKeys = ["code", "is_history"]
)
@TypeConverters(TroubleCodeEntity.TroubleCodeTypeConverter::class)
data class TroubleCodeEntity(
    @ColumnInfo(name = "code")
    val code: String,
    val description: String,
    @ColumnInfo(name = "rus_translate")
    val rusTranslate: String?,
    val type: TroubleCodeType,
    @ColumnInfo(name = "detection_time")
    val detectionTime: Long = 0L,
    @ColumnInfo(name = "is_history")
    val isHistory: Boolean = false
) {


    object TroubleCodeTypeConverter {
        @TypeConverter
        fun toType(ordinal: Int) = TroubleCodeType.values()[ordinal]

        @TypeConverter
        fun fromType(type: TroubleCodeType) = type.ordinal
    }
}

fun TroubleCodeEntity.toTroubleCode(): TroubleCode {
    val date = if(detectionTime > 0){
       SimpleDateFormat(
            "mm-HH-dd-MM-yy", Locale.getDefault()
        ).format(Date(detectionTime))
    } else {
        null
    }
    return TroubleCode(
        code,
        description,
        rusTranslate,
        type,
        date
    )
}