package ru.barinov.obdroid.ui.sensorsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.data.CommandsRepository
import ru.barinov.obdroid.domain.toPidCommand

class SensorsViewModel(
    private val commandsRepository: CommandsRepository
): ViewModel() {

    val commandsFlow = commandsRepository.getAllCommands().map { list->
        list.map { it.toPidCommand() }
    }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)


}