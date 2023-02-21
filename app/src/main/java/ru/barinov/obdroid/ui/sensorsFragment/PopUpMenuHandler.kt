package ru.barinov.obdroid.ui.sensorsFragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.domain.AtCommand
import ru.barinov.obdroid.ui.uiModels.PidCommand
import java.util.Locale

class PopUpMenuHandler(
    private val context: Context
) {

    fun buildPopUp(
        item: Command,
        itemView: View,
        favHandler: (Command, Boolean) -> (Unit)
    ) {
        val popUp = PopupMenu(context, itemView)
        popUp.inflate(R.menu.command_item_menu)
        if (item is PidCommand) {
            if (!item.isFavorite) {
                popUp.menu.getItem(0).title = context.getString(R.string.add_fav)
            } else popUp.menu.getItem(0).title = context.getString(R.string.remove_fav)
        } else if(item is AtCommand){
            if (!item.isFavorite) {
                popUp.menu.getItem(0).title = context.getString(R.string.add_fav)
            } else popUp.menu.getItem(0).title = context.getString(R.string.remove_fav)
        }
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.handle_favorites -> {
                    if (item is PidCommand) {
                        favHandler.invoke(item, !item.isFavorite)
                    } else if(item is AtCommand){
                        favHandler.invoke(item, !item.isFavorite)
                    }
                }
                else -> {
                    if (item is PidCommand) {
                        val description =
                            if (Locale.getDefault().language == "ru") item.commandDescRus
                            else item.commandDescEng
                        description?.let { desc->
                            Snackbar.make(context, itemView, desc, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
            true
        }
        popUp.show()
    }
}