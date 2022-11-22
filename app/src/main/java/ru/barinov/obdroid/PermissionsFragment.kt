package ru.barinov.obdroid

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding

class PermissionsFragment : Fragment() {

    private lateinit var binding: PermissionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PermissionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PermissionsUtil.apply {
            bindBackGroundLocationLauncher(
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    locationResultFlow.value = PermissionType.BackGroundLocation(it)
                }
            )
            bindRuntimeLocationLauncher(
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    PermissionsUtil.locationResultFlow.value = PermissionType.RuntimeLocation(
                        it.getOrDefault(ACCESS_FINE_LOCATION, false) &&
                                it.getOrDefault(ACCESS_COARSE_LOCATION, false)
                    )
                })
            if (hasLocationPermission(requireContext()) && hasBackgroundLocation(requireContext())) {
                rebase()
            } else {
                subscribe()
                binding.onStartButton.setOnClickListener {
                    rebase()
                }
                binding.locationPermissionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        requestLocationPermission()
                    }
                }
            }


//        PermissionsUtil.requestLocationPermission()
//        PermissionsUtil.requestBackgroundLocationPermission(requireActivity())
        }


    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            PermissionsUtil.apply {
                locationResultFlow.collectLatest { result ->
                    result?.let {
                        when (it) {
                            is PermissionType.BackGroundLocation -> {}
                            is PermissionType.RuntimeLocation -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                } else {

                                }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun rebase() {
        (requireActivity() as MainActivity).unlockDrawer()
        val graph = findNavController().navInflater.inflate(R.navigation.app_navigation)
        graph.setStartDestination(R.id.homeFragment)
        findNavController().graph = graph
    }
}