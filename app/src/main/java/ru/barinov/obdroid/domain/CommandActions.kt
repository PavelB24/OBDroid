package ru.barinov.obdroid.domain

import android.view.View
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.ui.sensorsFragment.PopUpMenuHandler

interface CommandActions {

    fun onLongClick(item: Command, itemView: View)

    fun onClick(item: Command)


}