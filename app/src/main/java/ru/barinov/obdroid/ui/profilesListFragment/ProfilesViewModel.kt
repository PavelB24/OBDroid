package ru.barinov.obdroid.ui.profilesListFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.barinov.obdroid.data.ProfilesRepository
import ru.barinov.obdroid.domain.ProfileEntity
import ru.barinov.obdroid.domain.toProfile

class ProfilesViewModel(
    private val repository: ProfilesRepository
): ViewModel() {

    val profilesFlow = repository.getProfiles().map {
        it.map { entity ->
            entity.toProfile()
        }
    }

    fun selectProfile(oldName: String, newName: String){
        viewModelScope.launch {
            repository.selectNewProfile(oldName, newName)
        }
    }

    fun createProfile(
        name: String,
        settingsChain: String,
        commandsChain: String,
        ordinal: Int

    ) {
        viewModelScope.launch {
            repository.addProfile(
                ProfileEntity(
                    name,
                    settingsChain,
                    commandsChain,
                    ordinal,
                    isSelected = false,
                    isDeletable = true
                )
            )
        }
    }

    fun saveDefaultProtoByOrdinal(ordinal: Int) {
        viewModelScope.launch {
            repository.updateDefaultProto(ordinal)
        }
    }


}