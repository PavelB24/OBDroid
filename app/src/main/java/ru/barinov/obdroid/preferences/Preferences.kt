package ru.barinov.obdroid.preferences

import android.content.SharedPreferences
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.R

class Preferences(sharedPreferences: SharedPreferences) {

    private companion object{
        const val VERSION = "version"
        const val TERMINAL = "terminal"
        const val WIFI_PORT = "wfPort"
        const val WIFI_ADDRESS = "wfAdress"
        const val SUPPORTED_FLAG = "onlySupported"
        const val WARM_STARTS = "useWarmStarts"
        const val COMMANDS_SORT = "commandsSort"
        const val SHOW_ONLY_FAVS_COMMANDS = "favsCommands"
        const val SHELL_THEME = "shellTheme"
    }

    var version by sharedPreferences.string(VERSION, BuildConfig.VERSION_NAME)
    var useTerminal by sharedPreferences.boolean(TERMINAL, false)
    var wifiPort by sharedPreferences.string(WIFI_PORT, "35000")
    var wifiAddress by sharedPreferences.string(WIFI_ADDRESS, null)
    var showOnlySupported by sharedPreferences.boolean(SUPPORTED_FLAG, false)
    var useWarmStarts by sharedPreferences.boolean(WARM_STARTS, false)
    var commandsSort by sharedPreferences.int(COMMANDS_SORT, 0)
    var showFavs by sharedPreferences.boolean(SHOW_ONLY_FAVS_COMMANDS, false)
    var savedShellThemeId by sharedPreferences.int(SHELL_THEME, R.drawable.fragment_common_background)
}