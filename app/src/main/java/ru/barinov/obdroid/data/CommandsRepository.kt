package ru.barinov.obdroid.data

import android.util.Log
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

    fun getAllCommands() = dao.getAllCommands()

    fun getCommandsByCategory(category: CommandCategory) =
        dao.getCommandsByCategory(category.ordinal)

    fun getOnlyFavs() = dao.getOnlyFavs()

    fun getCommandsByCategoryOnlyFavs(category: CommandCategory) =
        dao.getCommandsByCategoryOnlyFavs(category.ordinal)

    suspend fun handleFav(command: Command, isFav: Boolean) {
        if (command is PidCommand) {
            dao.handlePidFav(isFav, command.commandSectionHex, command.command)
        } else if (command is AtCommand) {
            dao.handleAtFav(isFav, command.command)
        }
    }


}