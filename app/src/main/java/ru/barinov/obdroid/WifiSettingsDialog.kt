package ru.barinov.obdroid

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.databinding.WifiConnectionSettingsDialogBinding

class WifiSettingsDialog : DialogFragment() {

    private val viewModel by viewModel<WifiConnectionSettingsViewModel>()
    private lateinit var binding: WifiConnectionSettingsDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WifiConnectionSettingsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        retainInstance = true
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle("")
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.isConnectedToNetwork()){
            binding.getawayField.visibility = View.GONE
            binding.negativeButton.visibility = View.GONE
        } else  binding.negativeButton.setOnClickListener {

        }
        binding.positiveButton.setOnClickListener {

        }
    }
}