package ru.barinov.obdroid.preferences

import android.content.SharedPreferences
import ru.barinov.obdroid.BuildConfig

class Preferences(sharedPreferences: SharedPreferences) {

    private companion object{
        const val VERSION = "version"
        const val TERMINAL = "terminal"
        const val LAUNCH = "firstLaunch"
    }

    val version by sharedPreferences.string(VERSION, BuildConfig.VERSION_NAME)
    val useTerminal by sharedPreferences.boolean(TERMINAL, false)
    val isFirstLaunch by sharedPreferences.boolean(LAUNCH, true)

}