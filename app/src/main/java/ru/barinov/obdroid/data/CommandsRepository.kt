package com

import ru.barinov.obdroid.data.CommandsDao
import ru.barinov.obdroid.domain.CommandEntity

class CommandsRepository(
    private val dao: CommandsDao
) {

    suspend fun insertCommand(command: CommandEntity) = dao.insertCommand(command)
}