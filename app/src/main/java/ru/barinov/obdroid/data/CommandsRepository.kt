package ru.barinov.obdroid.data

import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.CommandEntity

class CommandsRepository(
    private val dao: CommandsDao
) {

    suspend fun insertCommand(command: CommandEntity) = dao.insertCommand(command)

    suspend fun populateWithCommands(command: List<CommandEntity>) = dao.populateWithCommands(command)

    fun getAllCommands() = dao.getAllCommands()

    fun getCommandsByCategory(category: CommandCategory) =
        dao.getCommandsByCategory(category.ordinal)

    fun getOnlyFavs() = dao.getOnlyFavs()

    fun getCommandsByCategoryOnlyFavs(category: CommandCategory) =
        dao.getCommandsByCategoryOnlyFavs(category.ordinal)

    suspend fun handleFav(isFav: Boolean, section: String, command: String) =
        dao.handleFav(isFav, section, command)

}