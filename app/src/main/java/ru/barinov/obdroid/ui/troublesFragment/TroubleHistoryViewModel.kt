package ru.barinov.obdroid.ui.troublesFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.barinov.obdroid.data.TroublesRepository
import ru.barinov.obdroid.domain.TroubleCodeType
import ru.barinov.obdroid.preferences.Preferences

class TroubleHistoryViewModel(
    private val prefs: Preferences,
    private val troublesRepository: TroublesRepository
) : ViewModel() {


    private val pageTypeFlow = MutableStateFlow(TroublePageType.DETECTED)

    private val selectedCategoryType = MutableStateFlow(
        TroubleCodeType.values()[prefs.troubleCodeHistoryType]
    )

    private val searchFlow = MutableStateFlow("")

    private val timeLimitFlow = MutableStateFlow(0L)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val troubleCodesFlow = pageTypeFlow.flatMapLatest { type ->
        searchFlow.flatMapLatest { searchBy ->
            timeLimitFlow.flatMapLatest { time ->
                selectedCategoryType.flatMapLatest { category ->
                    if (type == TroublePageType.ALL_KNOWN) {
                        troublesRepository.getAllKnownTroublesByType(category, searchBy)
                    } else {
                        troublesRepository.getHistoryTroubles(time, category, searchBy, category)
                    }
                }
            }
        }
    }


    fun changeTimeLimit(targetTime: Long){
        viewModelScope.launch {
            timeLimitFlow.emit(System.currentTimeMillis() - targetTime)
        }
    }


    fun changePageType(type: TroublePageType) {
        viewModelScope.launch {
            pageTypeFlow.emit(type)
        }
    }

    fun changeSelectedType(type: TroubleCodeType) {
        viewModelScope.launch {
            selectedCategoryType.emit(type)
        }
    }

}