package ru.barinov.obdroid.base

class SearchHelper(
    private val receiver: SearchHandler
) : ExtendedSearchListener {


    override fun onSearchClosed() {
        receiver.onNewQuery("")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            receiver.onNewQuery(it)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            receiver.onNewQuery(it)
        }
        return false

    }
}