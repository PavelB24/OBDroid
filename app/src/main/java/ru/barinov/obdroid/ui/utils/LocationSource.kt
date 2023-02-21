package ru.barinov.obdroid.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object LocationSource {


    private val _locationFlow: MutableSharedFlow<Location> = MutableSharedFlow()
    val locationFlow: SharedFlow<Location> = _locationFlow

    private val gpsListener = GpsLocationListener()
    private val netListener = NetLocationListener()

    private var locationManager: LocationManager? = null

    private val localScope = CoroutineScope(Job() + Dispatchers.IO)


    private fun initialize(context: Context) {
        if (locationManager == null) {
            locationManager = context.getSystemService(LocationManager::class.java)
        }
    }


    fun startListening(context: Context): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initialize(context)
            locationManager?.let {
                it.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, gpsListener)
                it.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, netListener)
            }
            true
        } else false
    }

    fun getBestLocation(): Location?{
        val currGps = gpsListener.currentLocation
        val currNet = netListener.currentLocation
        return when{
            currGps != null && currNet == null -> currGps
            currGps == null && currNet != null -> currNet
            currGps != null && currNet != null -> {
                if(currGps.accuracy < currNet.accuracy){
                    currGps
                } else {
                    currNet
                }
            }
            else -> null
        }
    }


    class GpsLocationListener : LocationListener {

        var currentLocation: Location? = null

        override fun onLocationChanged(location: Location) {
            currentLocation = location
            localScope.launch {
                _locationFlow.emit(location)
            }
        }

    }

    class NetLocationListener : LocationListener {

        var currentLocation: Location? = null

        override fun onLocationChanged(location: Location) {
            currentLocation = location
            localScope.launch {
                _locationFlow.emit(location)
            }
        }

    }
}