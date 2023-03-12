package ru.barinov.obdroid.base

import androidx.appcompat.widget.SearchView


interface ExtendedSearchListener: SearchView.OnQueryTextListener {

    fun onSearchClosed()
}