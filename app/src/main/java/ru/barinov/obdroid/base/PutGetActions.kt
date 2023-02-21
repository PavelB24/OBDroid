package ru.barinov.obdroid.base

import android.app.Application
import ru.barinov.obdroid.ui.ObdApp

interface PutGetActions<T> {

    fun getValue(): T

    fun saveValue(value: T)
}
