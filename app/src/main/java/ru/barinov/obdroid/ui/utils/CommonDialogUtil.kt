package ru.barinov.obdroid.ui.utils

import android.app.AlertDialog
import android.content.Context
import ru.barinov.obdroid.R

object CommonDialogUtil {


    fun showCantOrFailConnectDialog(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.on_cant_connect))
            .setPositiveButton(android.R.string.ok){ di, i ->
                di.dismiss()
            }
            .show()
    }
}