package ru.barinov.obdroid.ui.startFragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.*
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding
import ru.barinov.obdroid.utils.StartPermissionsUtil

class PermissionsFragment : Fragment() {

    private lateinit var binding: PermissionsFragmentBinding
    private val permissionsUtil by lazy { StartPermissionsUtil() }
    private val viewModel: PermissionsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PermissionsFragmentBinding.inflate(inflater, container, false)
        permissionsUtil.initLaunchers(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (permissionsUtil.hasNecessaryPermissions(requireContext())) {
            lifecycleScope.launchWhenStarted {
                rebase()
            }
        } else {
            subscribe()
            initStates()
            lifecycleScope.launchWhenStarted {
                (requireActivity() as MainActivity).hideToolbar()
            }
        }


    }

    private fun initStates() {
        with(binding) {
            onStartButton.setOnClickListener {
                PermissionViewHelper.animateRebase(binding) {
                    rebase()
                }
            }
            headImage.setOnClickListener {
                PermissionViewHelper.animateLogo(headImage.drawable as AnimatedVectorDrawable)
            }
            permissionsUtil.apply {
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
                if (hasDozeOff(requireContext())) {
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
            permissionsUtil.apply {
                resultFlow.collectLatest { result ->
                    PermissionViewHelper.animateLogo(binding.headImage.drawable as AnimatedVectorDrawable)
                    result?.let {
                        if (it.granted) {
                            handlePositive(it)
                        } else handleNegative(it)
                        PermissionViewHelper.apply {
                            if (hasAllPermissions(requireContext())) {
                                animateRebase(binding) {
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
                    dozePermissionSwitch.isChecked = false
                }
                is PermissionType.FileSystemPermission -> fileSystemSwitch.isChecked = false
                is PermissionType.RuntimeLocation -> locationPermissionSwitch.isChecked = false
                is PermissionType.WiFiPermission -> TODO()
            }
        }
    }


    private fun handlePositive(type: PermissionType) {
        when (type) {
            is PermissionType.BackGroundLocation -> {
                PermissionViewHelper.hideLocationAnimate(binding)
                if (permissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionViewHelper.showButtonAnimate(
                        binding.onStartButtonFrame,
                        binding.onStartButton
                    )
                }
            }
            is PermissionType.RuntimeLocation -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissionsUtil.requestBackgroundLocationPermission()
                } else {
                    PermissionViewHelper.hideLocationAnimate(binding)
                    if (permissionsUtil.hasNecessaryPermissions(requireContext())) {
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
                if (permissionsUtil.hasNecessaryPermissions(requireContext())) {
                    PermissionViewHelper.showButtonAnimate(
                        binding.onStartButtonFrame,
                        binding.onStartButton
                    )
                }
            }
            is PermissionType.Doze -> {
                PermissionViewHelper.hideDozeAnimate(binding)
            }
            is PermissionType.FileSystemPermission -> {
                PermissionViewHelper.hideExternalStorageAnimate(binding)
            }
            is PermissionType.WiFiPermission -> TODO()
        }
    }

    private fun rebase() {
        Log.d("@@@", "REBASE")
        permissionsUtil.free()
        (requireActivity() as MainActivity).apply {
            startService()
            unlockDrawer()
            showToolbar()
            setupToolbar()
        }
    }
}