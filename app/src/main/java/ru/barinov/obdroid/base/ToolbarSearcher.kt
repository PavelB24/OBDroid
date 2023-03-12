package ru.barinov.obdroid.base

import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import ru.barinov.obdroid.R

interface ToolbarSearcher: MenuProvider {

    fun initSearch(
        menu: Menu,
        inflater: MenuInflater,
        searchListener: SearchHelper
    ){
        inflater.inflate(R.menu.searcher_menu, menu)
        val searchView = menu.findItem(R.id.search_item_menu).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
//        val searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button) as ImageView
//        searchIcon.setImageResource(R.drawable.ic_search)
        searchView.setOnQueryTextListener(searchListener)
    }
}