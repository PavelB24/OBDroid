package ru.barinov.obdroid

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow


object PermissionsUtil {

    private var runtimeLocLauncher : ActivityResultLauncher<Array<String>>? = null
    private var backGroundLocLauncher : ActivityResultLauncher<String>? = null
    val locationResultFlow : MutableStateFlow<PermissionType?> = MutableStateFlow(null)

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