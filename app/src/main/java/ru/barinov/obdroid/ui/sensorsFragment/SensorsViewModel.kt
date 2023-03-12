package ru.barinov.obdroid.ui.sensorsFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.data.CommandsRepository
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.preferences.Preferences

class SensorsViewModel(
    private val commandsRepository: CommandsRepository,
    private val prefs: Preferences
) : ViewModel(), SensorsToolbarActionsHandler {

    private val showFavsFlow = MutableStateFlow(prefs.showFavs)

    private val commandsCatFlow = MutableStateFlow(CommandCategory.values()[prefs.commandsSort])

    override val searchQueryFlow: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val commandsFlow = commandsCatFlow.flatMapLatest { category ->
        showFavsFlow.flatMapLatest { showFavs ->
            searchQueryFlow.flatMapLatest { query ->
                if (category == CommandCategory.UNDEFINED) {
                    if (showFavs) {
                        commandsRepository.getOnlyFavs(query)
                    } else commandsRepository.getAllCommandsByQuery(query)
                } else {
                    if (showFavs) {
                        commandsRepository.getCommandsByCategoryOnlyFavs(category, query)
                    } else commandsRepository.getCommandsByCategory(category, query)
                }
            }
        }
    }.map { list->
        list.map { (it.toHandledCommand())}
    }

    override fun onCategoryChanged(category: CommandCategory) {
        prefs.commandsSort = category.ordinal
        commandsCatFlow.value = category
    }

    override fun onFavButtonClicked(showFavs: Boolean) {
        prefs.showFavs = showFavs
        showFavsFlow.value = showFavs
    }

    override fun onNewQuery(query: String) {
        Log.d("@@@", "$query")
        searchQueryFlow.value = query
    }

    override fun getSavedSortState() = prefs.commandsSort

    override fun getSavedFavsState() = prefs.showFavs

    fun handleFav(command: Command, favFlag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            commandsRepository.handleFav(command, favFlag)
        }
    }


}