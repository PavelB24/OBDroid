package ru.barinov.obdroid.data

import ru.barinov.obdroid.domain.AtCommandEntity
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.CommandEntity

class CommandsRepository(
    private val dao: CommandsDao
) {

    suspend fun insertCommand(command: CommandEntity) = dao.insertCommand(command)

//    fun getCommandByHexAndSection(section: String, command: String) = dao.

    suspend fun populateWithCommands(commands: List<CommandEntity>) =
        dao.populateWithCommands(commands)

    @JvmName("populateWithAtCommands")
    suspend fun populateWithCommands(commands: List<AtCommandEntity>) =
        dao.populateWithAtCommands(commands)

    fun getAllCommands() = dao.getAllCommands()

    fun getCommandsByCategory(category: CommandCategory) =
        dao.getCommandsByCategory(category.ordinal)

    fun getOnlyFavs() = dao.getOnlyFavs()

    fun getCommandsByCategoryOnlyFavs(category: CommandCategory) =
        dao.getCommandsByCategoryOnlyFavs(category.ordinal)

    suspend fun handleFav(isFav: Boolean, section: String, command: String) =
        dao.handleFav(isFav, section, command)

}