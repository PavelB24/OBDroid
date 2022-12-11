package ru.barinov.obdroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.barinov.obdroid.domain.PidCommandEntity
import ru.barinov.obdroid.domain.TroubleEntity

@Database(
    version = 1,
    entities = [TroubleEntity::class, PidCommandEntity::class]
)
abstract class DataBase : RoomDatabase() {


}