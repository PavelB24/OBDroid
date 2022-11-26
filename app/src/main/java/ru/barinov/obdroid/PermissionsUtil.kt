package ru.barinov.obdroid

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow


object PermissionsUtil {

    val resultFlow: MutableStateFlow<PermissionType?> = MutableStateFlow(null)
    private var btLauncher: ActivityResultLauncher<Array<String>>? = null
    private var oldBtLauncher: ActivityResultLauncher<Intent>? = null
    private var dozeLauncher: ActivityResultLauncher<Intent>? = null
    private var locationLauncher: ActivityResultLauncher<Array<String>>? = null
    private var backLocationLauncher: ActivityResultLauncher<String>? = null
    private var externalLauncher: ActivityResultLauncher<String>? = null

    @MainThread
    fun initLaunchers(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backLocationLauncher =
                fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    resultFlow.value = PermissionType.BackGroundLocation(it)
                }
        }
        dozeLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                resultFlow.value = PermissionType.Doze(
                    result.resultCode == PackageManager.PERMISSION_GRANTED
                )
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btLauncher =
                fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    resultFlow.value = PermissionType.BluetoothPermission(
                        it.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false) &&
                                it.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)
                    )
                }
        } else {
            oldBtLauncher =
                fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    resultFlow.value = PermissionType.BluetoothPermission(
                        it.resultCode == PackageManager.PERMISSION_GRANTED
                    )
                }
        }
        externalLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                resultFlow.value = PermissionType.FileSystemPermission(it)
            }
        locationLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                resultFlow.value = PermissionType.RuntimeLocation(
                    it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) &&
                            it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                )
            }

    }

    fun hasLocationPermission(context: Context): Boolean =
        ContextCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    @MainThread
    fun hasBackgroundLocPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    @MainThread
    fun requestExternalStoragePermission() {
        externalLauncher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
    fun requestLocationPermission() {
        locationLauncher?.launch(
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


    fun hasBluetoothPermission(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            hasBTAdminPermission(context)
        }

    private fun hasBTAdminPermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) ==
                PackageManager.PERMISSION_GRANTED


    @MainThread
    fun requestBTPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btLauncher?.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else oldBtLauncher?.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    fun hasNecessaryPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            hasBTAdminPermission(context) && hasBluetoothPermission(context)
                    && hasLocationPermission(context) && hasBackgroundLocation(context)
        } else {
            hasBTAdminPermission(context) && hasLocationPermission(context)
                    && hasBackgroundLocation(context)
        }
    }


    @MainThread
    @SuppressLint("BatteryLife")
    fun requestDoze(context: Context) {
        val pm = context.getSystemService(PowerManager::class.java)
        if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
            dozeLauncher?.launch(Intent().apply {
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
    fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backLocationLauncher?.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

    }

}
