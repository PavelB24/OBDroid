package ru.barinov.obdroid.startFragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import ru.barinov.obdroid.*
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding
import ru.barinov.obdroid.preferences.Preferences

class PermissionsFragment : Fragment() {

    private lateinit var binding: PermissionsFragmentBinding
    private val prefs by inject<Preferences>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PermissionsFragmentBinding.inflate(inflater, container, false)
        PermissionsUtil.initLaunchers(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
            lifecycleScope.launchWhenStarted {
                rebase()
            }
        } else {
            subscribe()
            initStates()
        }

    }

    private fun initStates() {
        binding.onStartButton.setOnClickListener {
            rebase()
        }
        PermissionsUtil.apply {
            if (hasBluetoothPermission(requireContext())) {
                PermissionVisibilityUtil.hideBt(binding)
            } else {
                binding.btPermissionSwitch.setOnClickListener {
                    requestBTPermission()
                }
            }
            if (hasLocationPermission(requireContext()) && hasBackgroundLocation(requireContext())) {
                PermissionVisibilityUtil.hideLocation(binding)
            } else {
                binding.locationPermissionSwitch.setOnClickListener {
                    if (hasLocationPermission(requireContext())) {
                        requestBackgroundLocationPermission()
                    } else requestLocationPermission()
                }
            }
            if (hasDozeOff(requireContext()) || prefs.isDozeAsked) {
                PermissionVisibilityUtil.hideDoze(binding)
            } else {
                binding.dozePermissionSwitch.setOnClickListener {
                    requestDoze(requireContext())
                }
            }
            if (hasExternalStoragePermission(requireContext())) {
                PermissionVisibilityUtil.hideExternalStorage(binding)
            } else {
                binding.fileSystemSwitch.setOnClickListener {
                    requestExternalStoragePermission()
                }
            }
        }
    }


    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            PermissionsUtil.apply {
                resultFlow.collectLatest { result ->
                    result?.let {
                        if (it.granted) {
                            handlePositive(it)
                        } else handleNegative(it)
                    }
                }
            }
        }
    }

    private fun handleNegative(type: PermissionType) {

    }

    private fun handlePositive(type : PermissionType) {
        when (type) {
            is PermissionType.BackGroundLocation -> {
                PermissionVisibilityUtil.hideLocationAnimate(binding)
                if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionVisibilityUtil.showButtonAnimate(binding.onStartButtonFrame, binding.onStartButton)
                }
            }
            is PermissionType.RuntimeLocation -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    PermissionsUtil.requestBackgroundLocationPermission()
                } else {
                    PermissionVisibilityUtil.hideLocationAnimate(binding)
                    if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                        binding.line.visibility = GONE
                        PermissionVisibilityUtil.showButtonAnimate(binding.onStartButtonFrame, binding.onStartButton)
                    }
                }
            }
            is PermissionType.BluetoothPermission -> {
                PermissionVisibilityUtil.hideBtAnimate(binding)
                if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionVisibilityUtil.showButtonAnimate(binding.onStartButtonFrame, binding.onStartButton)
                }
            }
            is PermissionType.Doze -> {
                PermissionVisibilityUtil.hideDozeAnimate(binding)
                prefs.isDozeAsked = true
                Snackbar.make(requireView(), "", Snackbar.LENGTH_SHORT).show()
            }
            is PermissionType.FileSystemPermission -> {
                PermissionVisibilityUtil.hideExternalStorageAnimate(binding)
            }
            is PermissionType.WiFiPermission -> TODO()
        }
    }

    private fun rebase() {
        val graph = findNavController().navInflater.inflate(R.navigation.app_navigation)
        graph.setStartDestination(R.id.homeFragment)
        findNavController().graph = graph
        (requireActivity() as MainActivity).unlockDrawer()
    }
}