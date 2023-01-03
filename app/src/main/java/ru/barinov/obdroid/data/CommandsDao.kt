package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.barinov.obdroid.domain.CommandEntity

@Dao
interface CommandsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCommand(entity: CommandEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun populateWithCommands(commands: List<CommandEntity>)

    @Query("SELECT COUNT(*) FROM pid_commands")
    fun count() : Int

    @Query("SELECT * FROM pid_commands WHERE category != 1")
    fun getAllCommands(): Flow<List<CommandEntity>>

    @Query("SELECT * FROM pid_commands WHERE category=:categoryOrdinal AND category != 1")
    fun getCommandsByCategory(categoryOrdinal: Int): Flow<List<CommandEntity>>
}