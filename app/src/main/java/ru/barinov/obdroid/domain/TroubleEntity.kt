package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trouble_codes")
data class TroubleEntity(
    @PrimaryKey
    val code : String,
    @ColumnInfo(name = "description_eng")
    val descriptionEng : String,
    @ColumnInfo(name = "description_rus")
    val descriptionRus : String
)