package ru.barinov.obdroid

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ru.barinov.obdroid.ui.settings.SettingsFragmentViewModel

class WifiSettingsDialog : DialogFragment() {

    private val viewModel by activityViewModels<SettingsFragmentViewModel>()
}