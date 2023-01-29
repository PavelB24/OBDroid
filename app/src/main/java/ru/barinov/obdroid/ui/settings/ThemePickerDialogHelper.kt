package ru.barinov.obdroid.ui.settings

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.PutGetActions

class ThemePickerDialogHelper(
    private val context: Context,
    private val actions: PutGetActions<Int>
) {

    fun createThemePickerDialog(): AlertDialog {
        return AlertDialog.Builder(
            context,
            R.style.ThemePickerAlertDialog
        ).setView(
            Spinner(context).also {
                it.adapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,
                    context.resources.getStringArray(R.array.shell_themes_arr)
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                it.setSelection(getPositionById(actions.getValue()))
                it.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        actions.saveValue(convertPositionToId(position))
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                }
            }
        ).setPositiveButton(android.R.string.ok) { di, i ->
            di.dismiss()
        }.setTitle(context.getString(R.string.theme_picker_title))
            .create()
    }

    private fun convertPositionToId(position: Int): Int {
        return when(position){
            0 -> R.drawable.fragment_common_background
            1 -> R.drawable.dark_shell
            2 -> R.drawable.granat_shell
            else -> throw IllegalArgumentException("Illegal id")
        }
    }

    private fun getPositionById(value: Int): Int {
       return when(value){
           R.drawable.fragment_common_background -> 0
           R.drawable.dark_shell -> 1
           R.drawable.granat_shell -> 2
           else -> throw IllegalArgumentException("Wrong theme (drawable) ID")
       }
    }
}