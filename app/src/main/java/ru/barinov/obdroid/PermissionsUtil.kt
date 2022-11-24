package ru.barinov.obdroid

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.Equalizer.Settings
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.reflect.Method


object PermissionsUtil {

    val resultFlow: MutableStateFlow<PermissionType?> = MutableStateFlow(null)

    fun hasLocationPermission(context: Context): Boolean {
        val locPermissions =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locPermissions &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            locPermissions
        }
    }

    fun checkExternalStoragePermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED

    @MainThread
    fun requestExternalStoragePermission(fragment: Fragment) {
        val launcher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                resultFlow.value = PermissionType.FileSystemPermission(it)
            }
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    @SuppressLint("ObsoleteSdkInt")
    fun hasExternalStoragePermission(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                Environment.isExternalStorageManager()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
            else -> true
        }
    }

    @MainThread
    fun requestLocationPermission(fragment: Fragment) {
        val launcher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                resultFlow.value = PermissionType.RuntimeLocation(
                    it.containsKey(Manifest.permission.ACCESS_FINE_LOCATION) &&
                            it.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun hasBackgroundLocation(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }


    @RequiresApi(Build.VERSION_CODES.S)
    fun hasBluetoothPermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) ==
                PackageManager.PERMISSION_GRANTED

    fun hasBTAdminPermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) ==
                PackageManager.PERMISSION_GRANTED


    @MainThread
    fun requestBTPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val launcher =
                fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    resultFlow.value = PermissionType.BluetoothPermission(
                        it.containsKey(Manifest.permission.BLUETOOTH_SCAN) &&
                                it.containsKey(Manifest.permission.BLUETOOTH_CONNECT)
                    )
                }
            launcher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            val launcher =
                fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                }
            launcher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    @MainThread
    @SuppressLint("BatteryLife")
    fun requestDoze(fragment: Fragment, context: Context) {
        val pm = context.getSystemService(PowerManager::class.java)
        if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
            val launcher =
                fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    resultFlow.value = PermissionType.Doze(
                        result.resultCode == PackageManager.PERMISSION_GRANTED
                    )
                }
            launcher.launch(Intent().apply {
                action = android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:${context.packageName}")
            })
        }
    }

    fun hasDozeOff(context: Context): Boolean {
        val pm = context.getSystemService(PowerManager::class.java)
        return pm.isIgnoringBatteryOptimizations("package:${context.packageName}")
    }


    @MainThread
    fun requestBackgroundLocationPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val launcher =
                fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    resultFlow.value = PermissionType.BackGroundLocation(it)
                }
            launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

    }

}
