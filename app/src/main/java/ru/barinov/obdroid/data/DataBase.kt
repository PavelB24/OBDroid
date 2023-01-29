package ru.barinov.obdroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.barinov.obdroid.domain.AtCommandEntity
import ru.barinov.obdroid.domain.CommandEntity
import ru.barinov.obdroid.domain.ProfileEntity
import ru.barinov.obdroid.domain.TroubleCodeEntity

@Database(
    version = 1,
    entities = [
        CommandEntity::class,
        TroubleCodeEntity::class,
        AtCommandEntity::class,
        ProfileEntity::class
    ]
)
abstract class DataBase : RoomDatabase() {

    abstract fun getCommandsDao(): CommandsDao

    abstract fun getTroublesDao(): TroublesDao

    abstract fun getProfilesDao(): ProfilesDao


}