package ru.barinov.obdroid.ui


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.barinov.obdroid.core.RawDataProvider
import ru.barinov.obdroid.preferences.Preferences
import java.text.SimpleDateFormat
import java.util.*

class ShellViewModel(
    private val prefs: Preferences,
    private val obd: RawDataProvider
): ViewModel() {

     val rawDataFlow: SharedFlow<String?> = obd.getRawDataFlow()

    fun getSavedBackgroundId() = prefs.savedShellThemeId

    fun sendCommand(userInput: String) {
        obd.sendCommand(userInput)
    }

    fun getCurrentTimeAsString(): String{
        return SimpleDateFormat(
            "HH : mm", Locale.getDefault()
        ).format(
            Date(
                System.currentTimeMillis()
            )
        )
    }


}