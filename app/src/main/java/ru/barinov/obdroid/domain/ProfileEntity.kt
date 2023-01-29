package ru.barinov.obdroid.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.barinov.obdroid.ui.uiModels.Profile

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name = "at_command_chain")
    val atCommandChain: String?,
    @ColumnInfo(name = "command_chain")
    val commandsChain: String?,
    val protocol: Int,
    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean,
    @ColumnInfo(name = "is_deletable")
    val isDeletable: Boolean
)

fun ProfileEntity.toProfile(): Profile{
    return Profile(name, protocol, isSelected)
}
