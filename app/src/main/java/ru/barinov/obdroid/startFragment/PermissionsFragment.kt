package ru.barinov.obdroid.startFragment

import android.graphics.drawable.AnimatedVectorDrawable
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
import ru.barinov.obdroid.utils.PermissionsUtil

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
        with(binding) {
            onStartButton.setOnClickListener {
                rebase()
            }
            headImage.setOnClickListener {
                PermissionViewHelper.animateLogo(headImage.drawable as AnimatedVectorDrawable)
            }
            PermissionsUtil.apply {
                if (hasBluetoothPermission(requireContext())) {
                    PermissionViewHelper.hideBt(this@with)
                } else {
                    btPermissionSwitch.setOnClickListener {
                        requestBTPermission()
                    }
                }
                if (hasLocationPermission(requireContext()) && hasBackgroundLocation(requireContext())) {
                    PermissionViewHelper.hideLocation(this@with)
                } else {
                    locationPermissionSwitch.setOnClickListener {
                        if (hasLocationPermission(requireContext())) {
                            requestBackgroundLocationPermission()
                        } else requestLocationPermission()
                    }
                }
                if (hasDozeOff(requireContext()) || prefs.isDozeAsked) {
                    PermissionViewHelper.hideDoze(this@with)
                } else {
                    dozePermissionSwitch.setOnClickListener {
                        requestDoze(requireContext())
                    }
                }
                if (hasExternalStoragePermission(requireContext())) {
                    PermissionViewHelper.hideExternalStorage(this@with)
                } else {
                    fileSystemSwitch.setOnClickListener {
                        requestExternalStoragePermission()
                    }
                }
            }
        }
    }


    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            PermissionsUtil.apply {
                resultFlow.collectLatest { result ->
                    PermissionViewHelper.animateLogo(binding.headImage.drawable as AnimatedVectorDrawable)
                    result?.let {
                        if (it.granted) {
                            handlePositive(it)
                        } else handleNegative(it)
                        PermissionViewHelper.apply {
                            if (hasAllPermissions(requireContext(), prefs.isDozeAsked)) {
                                animateRebase(binding){
                                    rebase()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleNegative(type: PermissionType) {
        binding.apply {
            when (type) {
                is PermissionType.BackGroundLocation -> locationPermissionSwitch.isChecked = false
                is PermissionType.BluetoothPermission -> btPermissionSwitch.isChecked = false
                is PermissionType.Doze -> {
                    prefs.isDozeAsked = true
                    dozePermissionSwitch.isChecked = false
                }
                is PermissionType.FileSystemPermission -> fileSystemSwitch.isChecked = false
                is PermissionType.RuntimeLocation -> locationPermissionSwitch.isChecked = false
                is PermissionType.WiFiPermission -> TODO()
            }
        }
    }


    private fun handlePositive(type : PermissionType) {
        when (type) {
            is PermissionType.BackGroundLocation -> {
                PermissionViewHelper.hideLocationAnimate(binding)
                if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionViewHelper.showButtonAnimate(binding.onStartButtonFrame, binding.onStartButton)
                }
            }
            is PermissionType.RuntimeLocation -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    PermissionsUtil.requestBackgroundLocationPermission()
                } else {
                    PermissionViewHelper.hideLocationAnimate(binding)
                    if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                        binding.line.visibility = GONE
                        PermissionViewHelper.showButtonAnimate(
                            binding.onStartButtonFrame,
                            binding.onStartButton
                        )
                    }
                }
            }
            is PermissionType.BluetoothPermission -> {
                PermissionViewHelper.hideBtAnimate(binding)
                if (PermissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionViewHelper.showButtonAnimate(
                        binding.onStartButtonFrame,
                        binding.onStartButton
                    )
                }
            }
            is PermissionType.Doze -> {
                PermissionViewHelper.hideDozeAnimate(binding)
                prefs.isDozeAsked = true
                Snackbar.make(requireView(), "", Snackbar.LENGTH_SHORT).show()
            }
            is PermissionType.FileSystemPermission -> {
                PermissionViewHelper.hideExternalStorageAnimate(binding)
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