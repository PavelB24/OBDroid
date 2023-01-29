package ru.barinov.obdroid.ui


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.barinov.obdroid.preferences.Preferences

class ShellViewModel(
    private val prefs: Preferences
): ViewModel() {

    lateinit var rawDataFlow: MutableSharedFlow<String>

    fun getSavedBackgroundId() = prefs.savedShellThemeId


}