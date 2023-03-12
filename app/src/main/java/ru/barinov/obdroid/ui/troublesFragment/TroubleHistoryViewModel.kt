package ru.barinov.obdroid.ui.troublesFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.barinov.obdroid.data.TroublesRepository
import ru.barinov.obdroid.domain.TroubleCodeType
import ru.barinov.obdroid.preferences.Preferences

class TroubleHistoryViewModel(
    private val prefs: Preferences,
    private val troublesRepository: TroublesRepository
) : ViewModel(), TroubleHistoryToolbarActionsHandler {


    private val pageTypeFlow = MutableStateFlow(TroublePageType.DETECTED)


    override val searchQueryFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val selectedCategoryType = MutableStateFlow(
        TroubleCodeType.values()[prefs.troubleCodeHistoryType]
    )


    private val timeLimitFlow = MutableStateFlow(0L)


    @OptIn(ExperimentalCoroutinesApi::class)
    val troubleCodesFlow = pageTypeFlow.flatMapLatest { type ->
        searchQueryFlow.flatMapLatest { searchBy ->
            timeLimitFlow.flatMapLatest { time ->
                selectedCategoryType.flatMapLatest { category ->
                    if (type == TroublePageType.ALL_KNOWN) {
                        troublesRepository.getAllKnownTroublesByType(category, searchBy)
                    } else {
                        troublesRepository.getHistoryTroubles(time,  searchBy, category)
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO).cachedIn(viewModelScope)


    override fun onTroubleTypeChanged(type: TroubleCodeType) {
        viewModelScope.launch {
            selectedCategoryType.emit(type)
        }
    }

    override fun onModeChanged(type: TroublePageType) {
        viewModelScope.launch {
            pageTypeFlow.emit(type)
        }
    }

    fun changeTimeLimit(targetTime: Long){
        viewModelScope.launch {
            timeLimitFlow.emit(System.currentTimeMillis() - targetTime)
        }
    }


    override fun onNewQuery(query: String) {
        searchQueryFlow.value = query
    }

}