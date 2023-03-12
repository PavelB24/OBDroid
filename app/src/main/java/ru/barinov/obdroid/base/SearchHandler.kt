package ru.barinov.obdroid.base

import kotlinx.coroutines.flow.MutableStateFlow

interface SearchHandler {

    val searchQueryFlow: MutableStateFlow<String>

    fun onNewQuery(query: String)
}