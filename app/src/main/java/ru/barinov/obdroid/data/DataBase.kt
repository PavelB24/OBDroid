package ru.barinov.obdroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.barinov.obdroid.domain.CommandEntity
import ru.barinov.obdroid.domain.TroubleEntity

@Database(
    version = 1,
    entities = [TroubleEntity::class, CommandEntity::class]
)
abstract class DataBase : RoomDatabase() {

    abstract fun getCommandsDao() : CommandsDao


}