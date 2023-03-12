package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.barinov.obdroid.domain.AtCommandEntity
import ru.barinov.obdroid.domain.CommandEntity
import ru.barinov.obdroid.domain.PidCommandEntity

@Dao
interface CommandsDao {

    companion object {
        const val pidQuery = "SELECT hex_value as command_body," +
        " octal_value, command_section_hex, category, command_desc_eng, command_desc_rus," +
        " is_favorite, is_custom_command, measurement_unit, available as accessibility, is_dynamic," +
        " 1 as is_pid FROM pid_commands"

        const val atQuery = "SELECT command as command_body, null as octal_value, null as command_section_hex," +
        "  category, command_desc_eng, command_desc_rus,  is_favorite, 0 as is_custom_command, measurement_unit," +
        " accessibility, is_dynamic, 0 as is_pid FROM at_commands"

        const val searchQuery = "(:searchBy = ''" +
                " OR ((command_desc_rus LIKE '%'|| :searchBy || '%')" +
                " OR (command_desc_eng LIKE '%'|| :searchBy || '%')))"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCommand(entity: PidCommandEntity)

//    @Query("SELECT * FROM pid_commands WHERE hex_value =:hex AND command_section_hex =:sectionHex")
    @Query("SELECT * FROM pid_commands WHERE hex_value =:hex AND command_section_hex =:sectionHex LIMIT 1")
    fun getCommandByHexAndSection(hex: String, sectionHex: String): Flow<PidCommandEntity>
    @Query("SELECT * FROM at_commands WHERE command =:command AND accessibility == 1 LIMIT 1")
    fun getAtCommands(command: String): Flow<AtCommandEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun populateWithPidCommands(commands: List<PidCommandEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun populateWithAtCommands(commands: List<AtCommandEntity>)

    @Query("SELECT COUNT(*) FROM pid_commands")
    fun count() : Int

    @Query("$pidQuery WHERE $searchQuery AND category != 2 UNION $atQuery WHERE $searchQuery AND category != 2 AND accessibility == 1")
    fun getAllCommandsByQuery(searchBy: String): Flow<List<CommandEntity>>

    @Query("$pidQuery WHERE $searchQuery AND category != 2 AND is_favorite == 1 UNION $atQuery WHERE $searchQuery AND category != 2 AND is_favorite == 1")
    fun getOnlyFavs(searchBy: String): Flow<List<CommandEntity>>

    @Query("$pidQuery WHERE $searchQuery AND category =:categoryOrdinal UNION $atQuery WHERE $searchQuery AND category =:categoryOrdinal AND accessibility == 1")
    fun getCommandsByCategory(categoryOrdinal: Int, searchBy: String): Flow<List<CommandEntity>>

    @Query("$pidQuery WHERE $searchQuery AND category =:categoryOrdinal AND is_favorite == 1 UNION $atQuery WHERE $searchQuery AND category =:categoryOrdinal AND accessibility == 1 AND is_favorite == 1")
    fun getCommandsByCategoryOnlyFavs(categoryOrdinal: Int, searchBy: String): Flow<List<CommandEntity>>

    @Query("UPDATE pid_commands SET is_favorite =:fav" +
            " WHERE command_section_hex =:section AND hex_value =:command")
    suspend fun handlePidFav(fav: Boolean, section: String, command: String)

    @Query("UPDATE at_commands SET is_favorite =:fav" +
            " WHERE command =:command")
    suspend fun handleAtFav(fav: Boolean, command: String): Int

}