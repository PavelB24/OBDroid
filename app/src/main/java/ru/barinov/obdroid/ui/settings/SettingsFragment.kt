package ru.barinov.obdroid.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.PrefsLayoutBinding

class SettingsFragment : Fragment() {


    private lateinit var binding: PrefsLayoutBinding

    private val viewModel by activityViewModel<SettingsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PrefsLayoutBinding.inflate(inflater, container, false)
        binding.toolbar.title = getString(R.string.settings)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setupWithNavController(findNavController())
        binding.terminalSwitch.isChecked = viewModel.getTerminalFlag()
        binding.terminalSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeTerminalEnabled(isChecked)
            (requireActivity() as MainActivity).changeTerminalVisibility()
        }
        binding.wifiSettingsCard.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_wifiSettingsDialog)
        }
    }






}