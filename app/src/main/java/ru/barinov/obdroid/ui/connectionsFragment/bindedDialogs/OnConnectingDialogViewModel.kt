package ru.barinov.obdroid.ui.connectionsFragment.bindedDialogs


import androidx.lifecycle.ViewModel
import ru.barinov.obdroid.core.ObdEventBus

class OnConnectingDialogViewModel(
): ViewModel() {

    val eventFlow = ObdEventBus.eventFlow
}