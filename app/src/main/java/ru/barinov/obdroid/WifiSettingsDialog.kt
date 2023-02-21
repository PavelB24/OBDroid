package ru.barinov.obdroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.databinding.WifiConnectionSettingsDialogBinding
import ru.barinov.obdroid.ui.utils.AddressTextWatcher

class WifiSettingsDialog : DialogFragment() {


    companion object{
        const val setUpKey = "qsu"
    }

    private val viewModel by viewModel<WifiConnectionSettingsViewModel>()
    private lateinit var binding: WifiConnectionSettingsDialogBinding
    private var quickSetUp = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        quickSetUp = requireArguments().getBoolean(setUpKey)
    }

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
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val isConnected = viewModel.isConnectedToNetwork()
            getawayEt.isEnabled = !isConnected
            positiveButton.isEnabled = isConnected
            val textWatcher =
                AddressTextWatcher(portEt, getawayEt, positiveButton)
            getawayEt.setText(viewModel.getGetaway())
            portEt.setText(viewModel.getPort().toString())
            portEt.addTextChangedListener(textWatcher)
            getawayEt.addTextChangedListener(textWatcher)
            negativeButton.setOnClickListener {
                if (viewModel.isConnectedToNetwork()) {
//                Toast.makeText(
//                    requireContext(),
//                    "You can change address and port later in settings",
//                    Toast.LENGTH_SHORT
//                ).show()
                    viewModel.connectWithWiFi()
                }
                dismiss()
            }
            positiveButton.setOnClickListener {
                viewModel.onNewSettings(
                    getawayEt.text.toString(),
                    portEt.text.toString(),
                    viewModel.isConnectedToNetwork(),
                    quickSetUp
                )
                dismiss()
            }
        }
    }
}