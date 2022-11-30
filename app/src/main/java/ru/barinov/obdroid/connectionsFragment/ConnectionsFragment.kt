package ru.barinov.obdroid.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.utils.PermissionsUtil
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding

class ConnectionsFragment : Fragment() {

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding : ConnectionsLayoutBinding

    private lateinit var wifiManager : WifiManager

    private val connectionsReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            success?.let {
                if(it){
                    doOnNewWifi()
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun doOnNewWifi() {
        if(PermissionsUtil.hasLocationPermission(requireContext())) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConnectionsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wifiManager = requireContext().getSystemService(WifiManager::class.java)
        requireActivity().registerReceiver(connectionsReceiver,
            IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        )
        wifiManager.startScan()

    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(connectionsReceiver)
        super.onDestroy()
    }
}