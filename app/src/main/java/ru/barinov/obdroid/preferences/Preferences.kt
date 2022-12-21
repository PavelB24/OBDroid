package ru.barinov.obdroid.preferences

import android.content.SharedPreferences
import ru.barinov.obdroid.BuildConfig

class Preferences(sharedPreferences: SharedPreferences) {

    private companion object{
        const val VERSION = "version"
        const val TERMINAL = "terminal"
        const val LAUNCH = "firstLaunch"
        const val DOZE = "doze"
        const val WIFI_PORT = "wfPort"
        const val SUPPORTED_FLAG = "onlySupported"
        const val WARM_STARTS = "useWarmStarts"
    }

    var version by sharedPreferences.string(VERSION, BuildConfig.VERSION_NAME)
    var isDozeAsked by sharedPreferences.boolean(DOZE, false)
    var useTerminal by sharedPreferences.boolean(TERMINAL, false)
    var isFirstLaunch by sharedPreferences.boolean(LAUNCH, true)
    var wifiPort by sharedPreferences.string(WIFI_PORT, "35000")
    var wifiAddress by sharedPreferences.string(WIFI_PORT, null)
    var showOnlySupported by sharedPreferences.boolean(SUPPORTED_FLAG, false)
    var useWarmStarts by sharedPreferences.boolean(WARM_STARTS, false)
}