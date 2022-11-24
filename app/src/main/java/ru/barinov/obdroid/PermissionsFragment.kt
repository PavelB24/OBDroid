package ru.barinov.obdroid

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding
import java.lang.reflect.Method

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
        subscribe()
        initStates()

    }

    private fun initStates() {
        PermissionsUtil.apply {
        }
    }


    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            PermissionsUtil.apply {
                resultFlow.collectLatest { result ->
                    result?.let {
                        when (it) {
                            is PermissionType.BackGroundLocation -> {}
                            is PermissionType.RuntimeLocation -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                    requestBackgroundLocationPermission()
                                } else {

                                }
                            }
                            is PermissionType.BluetoothPermission -> TODO()
                            is PermissionType.Doze -> TODO()
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