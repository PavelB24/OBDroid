package ru.barinov.obdroid.ui.profilesListFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.ItemInteractor
import ru.barinov.obdroid.databinding.ProfilesFragmentLayoutBinding
import ru.barinov.obdroid.ui.uiModels.Profile

class ProfilesFragment : Fragment() {

    private lateinit var binding: ProfilesFragmentLayoutBinding

    private val profilesAdapter by lazy { ProfilesAdapter() }

    private val viewModel: ProfilesViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfilesFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun initViews() {
        binding.addProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profilesFragment_to_profileCreationFragment)
        }
        binding.profilesRv.adapter = profilesAdapter.also {
            it.bindPickAction(object: ItemInteractor<Profile>{
                override fun onClick(item: Profile) {
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.select_profile_message))
                        .setPositiveButton(android.R.string.ok) { di, i ->
                            viewModel.selectProfile(
                                binding.currentProfile.profileName.text.toString(),
                                item.name
                            )
                            di.dismiss()
                        }
                        .setNegativeButton(android.R.string.cancel) { di, i ->
                            di.dismiss()
                        }
                        .show()
                }

                override fun onLongClick(item: Profile, view: View) {

                }
            })
        }
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.profilesFlow.onEach {
                profilesAdapter.updateList(it.filter { profile ->
                    if (profile.isSelected) {
                        ProfileViewHolder(
                            binding.currentProfile,
                            profile.name == "Auto"
                        ) {
                            viewModel.saveDefaultProtoByOrdinal(it)
                        }.apply {
                            onBind(profile)
                        }
                    }
                    return@filter !profile.isSelected
                })
            }.collect()
        }
    }


}