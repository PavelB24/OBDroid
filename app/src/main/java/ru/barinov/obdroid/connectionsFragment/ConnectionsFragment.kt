package ru.barinov.obdroid.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.MainActivity
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.core.toBtConnectionItem
import ru.barinov.obdroid.utils.PermissionsUtil
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem
import java.util.*

class ConnectionsFragment : Fragment() {

    private companion object {
        const val BT_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding: ConnectionsLayoutBinding

    private lateinit var wifiManager: WifiManager

    private lateinit var btManager: BluetoothManager

    private val connectionsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        doOnNewScanResults()
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    addDevice(device)
                }

            }

        }

    }

    private fun addDevice(device: BluetoothDevice?) {

        device?.let {
            adapter.newItems(
                ConnectionsListHandler.addBt(
                    it.toBtConnectionItem(object :
                        BtConnectionI {
                        @SuppressLint("MissingPermission")
                        override fun createBound() {
                            val bind = it
                            bind.createBond()
                        }

                        @SuppressLint("MissingPermission")
                        override fun connect() {
                            val uuid = UUID.fromString(BT_UUID)
                            val bind = it
                            bind.createRfcommSocketToServiceRecord(uuid)
                        }
                    })
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun doOnNewScanResults() {
        if (PermissionsUtil.hasLocationPermission(requireContext())) {
            val result = wifiManager.scanResults.map {
                WifiConnectionItem(
                    ConnectionItem.ConnectionType.WIFI,
                    it.BSSID,
                    it.frequency,
                    it.timestamp,
                    it.channelWidth,
                    it.SSID
                )
            }
            adapter.newItems(ConnectionsListHandler.addWiFi(result))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConnectionsLayoutBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).setSupportActionBar(binding.connectionsToolbar)
        binding.connectionsToolbar.setupWithNavController(findNavController())
        binding.connectionsToolbar.title = ""
        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.connecions_toolbar_menu, menu)
            }

            @SuppressLint("MissingPermission")
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.enable_bt_item -> {
                        if(BuildConfig.VERSION_CODE >= Build.VERSION_CODES.TIRAMISU){
                            //todo
                        } else {
                            btManager.adapter.enable()
                            btManager.adapter.startDiscovery()
                        }

                    }
                    R.id.enable_wifi_item -> {
                        wifiManager.startScan()
                    }

                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        wifiManager = requireContext().getSystemService(WifiManager::class.java)
        btManager = requireContext().getSystemService(BluetoothManager::class.java)
        requireActivity().registerReceiver(connectionsReceiver,
            IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        )
        wifiManager.startScan()
        btManager.adapter.startDiscovery()
//        requireContext().getSystemService(BluetoothManager::class.java).adapter.bluetoothLeScanner.startScan(object : ScanCallback(){
//            override fun onScanResult(callbackType: Int, result: ScanResult?) {
//                super.onScanResult(callbackType, result)
//                Log.d("@@@", result?.device.toString())
//                result?.let {
//                    addDevice(it.device)
//                }
//            }
//        })
    }

    private fun initViews() {
        binding.connectionsRv.adapter = adapter
        adapter.addItemClickListener(object : ConnectionsAdapter.ConnectionClickListener {
            override fun onItemClick(item: ConnectionItem, itemView: View) {
                val popup = PopupMenu(requireContext(), itemView)
                when (item) {
                    is BtConnectionItem -> {
                        popup.apply {
                            inflate(R.menu.bt_item_popup_menu)
                            setOnMenuItemClickListener {
                                when(it.itemId){
                                    R.id.bound_menuItem -> {}
                                    R.id.connect_bt_item -> {}
                                }
                                true
                            }
                        }
                    }
                    else -> {
                        popup.apply {
                            inflate(R.menu.wifi_item_popup_menu)
                            setOnMenuItemClickListener {
                                true
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(connectionsReceiver)
        super.onDestroy()
    }
}