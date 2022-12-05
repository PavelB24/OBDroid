package ru.barinov.obdroid.connectionsFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConnectionsViewModel() : ViewModel() {

    private val _onConnectFlow = MutableSharedFlow<ConnectionActionHandler.ConnectedEventType>().onEach {
        when(it){
            ConnectionActionHandler.ConnectedEventType.WIFI -> onConnect()
            ConnectionActionHandler.ConnectedEventType.BLUETOOTH -> onConnect()
            else -> { Unit }
        }
    }.shareIn(viewModelScope, SharingStarted.Eagerly)

    val onConnectFlow: SharedFlow<ConnectionActionHandler.ConnectedEventType> = _onConnectFlow

    private fun onConnect() {
        TODO("Not yet implemented")
    }




}