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
import android.os.PowerManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow


object PermissionsUtil {

    private var runtimeLocLauncher : ActivityResultLauncher<Array<String>>? = null
    private var backGroundLocLauncher : ActivityResultLauncher<String>? = null
    val resultFlow : MutableStateFlow<PermissionType?> = MutableStateFlow(null)
    private var btLauncher : ActivityResultLauncher<Array<String>>? = null
    private var oldBtLauncher :  ActivityResultLauncher<Intent>? = null

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

    fun bindRuntimeLocationLauncher(launcher: ActivityResultLauncher<Array<String>>){
        this.runtimeLocLauncher = launcher
    }

    fun bindBackGroundLocationLauncher(launcher: ActivityResultLauncher<String>){
        this.backGroundLocLauncher = launcher
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

    fun requestLocationPermission(){
        runtimeLocLauncher?.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    fun hasBackgroundLocation(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
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

    fun bindBTLauncher(launcher : ActivityResultLauncher<Array<String>>){
        this.btLauncher = launcher
    }

    fun bindOldBtLauncher(launcher : ActivityResultLauncher<Intent>){
        this.oldBtLauncher = launcher
    }


    fun requestBTPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btLauncher?.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            oldBtLauncher?.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    fun requestDoze(context: Context){

    }

    fun hasDozeOff(context: Context) : Boolean {
       val pm = context.getSystemService(PowerManager::class.java)
       return pm.isIgnoringBatteryOptimizations("package:${context.packageName}")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestBackgroundLocationPermission() {
        backGroundLocLauncher?.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ),
//                101
//            )
//        } else {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                101
//            )
//        }
    }


}