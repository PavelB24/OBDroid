package ru.barinov.obdroid.data

import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.domain.AtCommand
import ru.barinov.obdroid.domain.AtCommandEntity
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.PidCommandEntity
import ru.barinov.obdroid.ui.uiModels.PidCommand

class CommandsRepository(
    private val dao: CommandsDao
) {

    suspend fun insertCommand(command: PidCommandEntity) = dao.insertCommand(command)

//    fun getCommandByHexAndSection(section: String, command: String) = dao.

    suspend fun populateWithCommands(commands: List<PidCommandEntity>) =
        dao.populateWithPidCommands(commands)

    @JvmName("populateWithAtCommands")
    suspend fun populateWithCommands(commands: List<AtCommandEntity>) =
        dao.populateWithAtCommands(commands)

    fun getAllCommandsByQuery(query: String) = dao.getAllCommandsByQuery(query)

    fun getCommandsByCategory(category: CommandCategory, query: String) =
        dao.getCommandsByCategory(category.ordinal, query)

    fun getOnlyFavs(query: String) = dao.getOnlyFavs(query)

    fun getCommandsByCategoryOnlyFavs(category: CommandCategory, query: String) =
        dao.getCommandsByCategoryOnlyFavs(category.ordinal, query)

    suspend fun handleFav(command: Command, isFav: Boolean) {
        if (command is PidCommand) {
            dao.handlePidFav(isFav, command.commandSectionHex, command.command)
        } else if (command is AtCommand) {
            dao.handleAtFav(isFav, command.command)
        }
    }


}