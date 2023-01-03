package ru.barinov.obdroid.data

import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.CommandEntity

class CommandsRepository(
    private val dao: CommandsDao
) {

    suspend fun insertCommand(command: CommandEntity) = dao.insertCommand(command)

    suspend fun populateWithCommands(command: List<CommandEntity>) = dao.populateWithCommands(command)

    fun getAllCommands() = dao.getAllCommands()

    fun getCommandsByCategory(category: CommandCategory) = dao.getCommandsByCategory(category.ordinal)

}