package ru.barinov.obdroid.ui.settings

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.PutGetActions
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.PrefsLayoutBinding

class SettingsFragment : Fragment() {


    private lateinit var binding: PrefsLayoutBinding

    private val viewModel: SettingsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PrefsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
//        setAdvancedSettingsVisibility(viewModel.getAdvancedSettingsMenuState())
        binding.apply {
            terminalSwitch.isChecked = viewModel.getTerminalFlag()
            terminalSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.changeTerminalEnabled(isChecked)
                (requireActivity() as MainActivity).changeShellVisibility()
            }
            onlySupportedSwitch.isChecked = viewModel.getUseOnlySupported()
            onlySupportedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.changeUseOnlySupported(isChecked)
            }
            wsSwitch.isChecked = viewModel.getWarmStartsFlag()
            wsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.changeUseWarmStarts(isChecked)
            }
            profilesCard.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_profilesFragment)
            }
            terminalThemeCard.setOnClickListener {
                ThemePickerDialogHelper(requireContext(), object : PutGetActions<Int> {
                    override fun getValue(): Int {
                        return viewModel.getSavedThemeId()
                    }

                    override fun saveValue(value: Int) {
                        viewModel.saveShellThemeIdByOrdinal(value)
                    }

                }).createThemePickerDialog().show()
            }
        }
    }
}