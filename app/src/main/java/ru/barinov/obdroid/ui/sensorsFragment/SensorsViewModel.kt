package ru.barinov.obdroid.ui.sensorsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.data.CommandsRepository
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.toPidCommand
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.uiModels.PidCommand

class SensorsViewModel(
    private val commandsRepository: CommandsRepository,
    private val prefs: Preferences
) : ViewModel() {

    private val showFavsFlow = MutableStateFlow(prefs.showFavs)

    private val commandsCatFlow = MutableStateFlow(CommandCategory.values()[prefs.commandsSort])

    val commandsFlow = commandsCatFlow.flatMapLatest { category ->
        showFavsFlow.flatMapLatest { showFavs ->
            if (category == CommandCategory.UNDEFINED){
                if(showFavs) {
                    commandsRepository.getOnlyFavs()
                } else commandsRepository.getAllCommands()
            } else{
                if(showFavs) {
                    commandsRepository.getCommandsByCategoryOnlyFavs(category)
                } else commandsRepository.getCommandsByCategory(category)
            }
        }
    }.map { list->
        list.map { (it.toHandledCommand())}
    }

    fun changeCategory(category: CommandCategory) {
        prefs.commandsSort = category.ordinal
        commandsCatFlow.value = category
    }

    fun setShowFavs(showFavs: Boolean) {
        prefs.showFavs = showFavs
        showFavsFlow.value = showFavs
    }

    fun getSavedSortState() = prefs.commandsSort

    fun getSavedFavsState() = prefs.showFavs

    fun handleFav(command: Command, favFlag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            commandsRepository.handleFav(command, favFlag)
        }
    }


}