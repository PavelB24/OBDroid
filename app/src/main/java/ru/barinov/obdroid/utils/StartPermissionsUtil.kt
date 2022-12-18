package ru.barinov.obdroid.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.PermissionType


class StartPermissionsUtil {

    val resultFlow: MutableStateFlow<PermissionType?> = MutableStateFlow(null)
    private var btLauncher: ActivityResultLauncher<Array<String>>? = null
    private var oldBtLauncher: ActivityResultLauncher<Intent>? = null
    private var dozeLauncher: ActivityResultLauncher<Intent>? = null
    private var locationLauncher: ActivityResultLauncher<Array<String>>? = null
    private var backLocationLauncher: ActivityResultLauncher<String>? = null
    private var externalLauncher: ActivityResultLauncher<String>? = null
    private var newExternalLauncher: ActivityResultLauncher<Intent>? = null

    @MainThread
    fun initLaunchers(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backLocationLauncher =
                fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    resultFlow.value = PermissionType.BackGroundLocation(it)
                }
        }
        newExternalLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            resultFlow.value = PermissionType.FileSystemPermission(
                result.resultCode == PackageManager.PERMISSION_GRANTED
            )
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

    @MainThread
    fun hasLocationPermission(context: Context): Boolean =
        PermissionsChecker.hasLocationPermission(context)

    @MainThread
    fun hasBackgroundLocPermission(context: Context): Boolean =
        PermissionsChecker.hasBackgroundLocPermission(context)



    @MainThread
    fun requestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            newExternalLauncher?.launch(intent)
        } else externalLauncher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    @SuppressLint("ObsoleteSdkInt")
    fun hasExternalStoragePermission(context: Context): Boolean =
        PermissionsChecker.hasExternalStoragePermission(context)

    @MainThread
    fun requestLocationPermission() {
        locationLauncher?.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @MainThread
    fun hasBackgroundLocation(context: Context) =
        PermissionsChecker.hasBackgroundLocation(context)


    @MainThread
    fun hasBluetoothPermission(context: Context) =
        PermissionsChecker.hasBluetoothPermission(context)


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

    @MainThread
    fun hasNecessaryPermissions(context: Context): Boolean =
        PermissionsChecker.hasNecessaryPermissions(context)

    @MainThread
    fun hasAllPermissions(context: Context, dozeShown : Boolean) : Boolean =
        PermissionsChecker.hasAllPermissions(context, dozeShown)


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

    @MainThread
    fun hasDozeOff(context: Context): Boolean =
        PermissionsChecker.hasDozeOff(context)


    @MainThread
    fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backLocationLauncher?.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

    }

}
