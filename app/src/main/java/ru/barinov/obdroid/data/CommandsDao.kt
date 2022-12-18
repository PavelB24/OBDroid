package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.barinov.obdroid.domain.CommandEntity

@Dao
interface CommandsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCommand(entity: CommandEntity)

    @Query("SELECT COUNT(*) FROM pid_commands")
    fun count() : Int
}