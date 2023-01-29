package ru.barinov.obdroid.base

import android.view.View

interface ItemInteractor<T> {

    fun onClick(item: T)

    fun onLongClick(item: T, view: View)
}