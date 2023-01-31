package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.barinov.obdroid.domain.AtCommandEntity
import ru.barinov.obdroid.domain.CommandEntity

@Dao
interface CommandsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCommand(entity: CommandEntity)

//    @Query("SELECT * FROM pid_commands WHERE hex_value =:hex AND command_section_hex =:sectionHex")
    @Query("SELECT * FROM pid_commands WHERE hex_value =:hex AND command_section_hex =:sectionHex LIMIT 1")
    fun getCommandByHexAndSection(hex: String, sectionHex: String): Flow<CommandEntity>
    @Query("SELECT * FROM at_commands WHERE command =:command AND accessibility == 1 LIMIT 1")
    fun getAtCommand(command: String): Flow<AtCommandEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun populateWithCommands(commands: List<CommandEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun populateWithAtCommands(commands: List<AtCommandEntity>)

    @Query("SELECT COUNT(*) FROM pid_commands")
    fun count() : Int

    @Query("SELECT * FROM pid_commands WHERE category != 2")
    fun getAllCommands(): Flow<List<CommandEntity>>

    @Query("SELECT * FROM pid_commands WHERE category != 2 AND is_fav == 1")
    fun getOnlyFavs(): Flow<List<CommandEntity>>

    @Query("SELECT * FROM pid_commands WHERE category =:categoryOrdinal ")
    fun getCommandsByCategory(categoryOrdinal: Int): Flow<List<CommandEntity>>

    @Query("SELECT * FROM pid_commands WHERE category =:categoryOrdinal AND is_fav == 1")
    fun getCommandsByCategoryOnlyFavs(categoryOrdinal: Int): Flow<List<CommandEntity>>

    @Query("UPDATE pid_commands SET is_fav =:fav" +
            " WHERE command_section_hex =:section AND hex_value =:command")
    suspend fun handleFav(fav: Boolean, section: String, command: String)

}