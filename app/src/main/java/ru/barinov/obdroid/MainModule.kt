package ru.barinov.obdroid

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.barinov.obdroid.preferences.Preferences

private const val SHARED_PREFS_NAME = "prefs"


val mainModule = module {

    single {
        androidApplication().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    single {
        Preferences(get())
    }

}