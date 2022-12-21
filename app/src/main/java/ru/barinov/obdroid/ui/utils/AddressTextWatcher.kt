package ru.barinov.obdroid.ui.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddressTextWatcher(
    private val address: TextInputEditText,
    private val port: TextInputEditText,
    private val button: MaterialButton
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        button.isEnabled = isFieldsFilled()
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun isFieldsFilled(): Boolean {
        port.text?.let {
            return it.matches("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$".toRegex()) &&
                    !address.text?.toString().isNullOrEmpty()
        }
        return false
    }
}