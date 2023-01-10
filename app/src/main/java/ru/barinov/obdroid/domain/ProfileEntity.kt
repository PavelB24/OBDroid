package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name = "command_chain")
    val commandChain: String
)
