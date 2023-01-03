package ru.barinov.obdroid.domain

import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.ui.sensorsFragment.PopUpMenuHandler

interface CommandActions {

    fun onLongClick(item: Command)

    fun onClick(item: Command)


}