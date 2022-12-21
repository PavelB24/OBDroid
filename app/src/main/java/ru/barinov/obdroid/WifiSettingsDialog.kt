package ru.barinov.obdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.databinding.WifiConnectionSettingsDialogBinding
import ru.barinov.obdroid.ui.utils.AddressTextWatcher

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
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isConnectedToNetwork()) {
            binding.getawayEt.isEnabled = false
        }
        val textWatcher =
            AddressTextWatcher(binding.portEt, binding.getawayEt, binding.positiveButton)
        binding.getawayEt.setText(viewModel.getGetaway())
        binding.portEt.setText(viewModel.getPort())
        binding.portEt.addTextChangedListener(textWatcher)
        binding.getawayEt.addTextChangedListener(textWatcher)
        binding.negativeButton.setOnClickListener {
            if (viewModel.isConnectedToNetwork()) {
                Toast.makeText(
                    requireContext(),
                    "You can change address and port later in settings",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dismiss()
        }
        binding.positiveButton.setOnClickListener {
            viewModel.onNewSettings(
                binding.getawayEt.text.toString(),
                binding.portEt.text.toString()
            )
            dismiss()
        }
    }


}