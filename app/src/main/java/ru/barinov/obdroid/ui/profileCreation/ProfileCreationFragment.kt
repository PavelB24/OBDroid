package ru.barinov.obdroid.ui.profileCreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.ProfileCreationLayoutBinding
import ru.barinov.obdroid.ui.profilesListFragment.ProfilesViewModel

class ProfileCreationFragment: DialogFragment() {


    private lateinit var binding: ProfileCreationLayoutBinding
    private val viewModel: ProfilesViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileCreationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            backButton.setOnClickListener { dismiss() }
            createProfile.setOnClickListener {
                if (!isMainFieldsEmpty()) {
                    viewModel.createProfile(
                        etProfileName.text.toString(),
                        etSettings.text.toString(),
                        etCommands.text.toString(),
                        protoList.selectedItemPosition
                    )
                    dismiss()
                } else {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.enter_name_and_settings),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isMainFieldsEmpty(): Boolean {
        return binding.etProfileName.text.isNullOrEmpty() &&
                binding.etSettings.text.isNullOrEmpty()
    }

}