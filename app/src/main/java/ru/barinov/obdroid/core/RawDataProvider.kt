package ru.barinov.obdroid.core

import kotlinx.coroutines.flow.SharedFlow

interface RawDataProvider {

   fun getRawDataFlow(): SharedFlow<String?>

   fun sendCommand(command: String)
}