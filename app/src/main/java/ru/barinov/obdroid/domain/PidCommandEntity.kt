package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "pid_commands")
data class PidCommandEntity(
    @ColumnInfo(name = "dex_pid")
    private val dexPid : String,
    @ColumnInfo(name = "dec_pid")
    private val decPid : Int,
    private val description : String
)