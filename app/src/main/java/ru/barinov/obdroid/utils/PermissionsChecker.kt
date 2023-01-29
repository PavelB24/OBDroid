package ru.barinov.obdroid.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.util.Log
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat

object PermissionsChecker {

    @MainThread
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


    @SuppressLint("ObsoleteSdkInt")
    fun hasExternalStoragePermission(context: Context): Boolean {
//        return when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
//                Environment.isExternalStorageManager()
//            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
//                ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//            }
//            else -> true
//        }
        return true
    }


    @MainThread
    fun hasBluetoothPermission(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            hasBTAdminPermission(context)
        }

    @MainThread
    fun hasBTAdminPermission(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) ==
                PackageManager.PERMISSION_GRANTED

    @MainThread
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
    fun hasBackgroundLocation(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    @MainThread
    fun hasDozeOff(context: Context): Boolean {
        val pm = context.getSystemService(PowerManager::class.java)
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    @MainThread
    fun hasAllPermissions(context: Context) : Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            hasBTAdminPermission(context) && hasBluetoothPermission(context)
                    && hasLocationPermission(context) && hasBackgroundLocation(context) &&
                    hasDozeOff(context)
//                    && hasExternalStoragePermission(context)
        } else {
            hasBTAdminPermission(context) && hasLocationPermission(context)
                    && hasLocationPermission(context) && hasBackgroundLocation(context) &&
                    hasDozeOff(context)
//                    && hasExternalStoragePermission(context)
        }
    }


}