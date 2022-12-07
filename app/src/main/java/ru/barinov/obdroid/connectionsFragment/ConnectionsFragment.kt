package ru.barinov.obdroid.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.MacAddress
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.ConnectedEventType
import ru.barinov.obdroid.MainActivity
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.core.toBtConnectionItem
import ru.barinov.obdroid.utils.PermissionsUtil
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding
import ru.barinov.obdroid.startFragment.PermissionViewHelper
import ru.barinov.obdroid.uiModels.BtConnectionItem
import ru.barinov.obdroid.uiModels.WifiConnectionItem
import java.net.Socket
import java.util.*

class ConnectionsFragment : Fragment() {

    private companion object {
        const val BT_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    //События для flow все перенеси в ресивер, кроме поключения по WF  - там коллбэк, обрабатывай метод коннекта, пытаясь открыть полученный соккет

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding: ConnectionsLayoutBinding

    private lateinit var wifiManager: WifiManager

    private lateinit var btManager: BluetoothManager

    private val viewModel by viewModel<ConnectionsViewModel>()

    private val connectionsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

    }

    private fun addDevice(device: BluetoothDevice?) {
        device?.let {
            adapter.newItems(
                viewModel.handle(
                    it.toBtConnectionItem(object :
                        BtConnectionI {
                        @SuppressLint("MissingPermission")
                        override fun createBound() : Boolean {
                            val bind = it
                            return bind.createBond()
                        }

                        @SuppressLint("MissingPermission")
                        override fun connect(): BluetoothSocket {
                            val uuid = UUID.fromString(BT_UUID)
                            val bind = it
                            return bind.createInsecureRfcommSocketToServiceRecord(uuid)
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
            adapter.newItems(viewModel.handle(result))
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
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.connecions_toolbar_menu, menu)
            }
            @SuppressLint("MissingPermission")
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.enable_bt_item -> {
                        if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.TIRAMISU) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wifiManager = requireContext().getSystemService(WifiManager::class.java)
        btManager = requireContext().getSystemService(BluetoothManager::class.java)
        subscribe()
        initViews()
        requireActivity().registerReceiver(connectionsReceiver,
            IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            }
        )
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

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.onConnectFlow.onEach { event->
                when(event){
                    is ConnectedEventType.BluetoothBounded -> {

                    }
                    is ConnectedEventType.BluetoothConnected -> {
                        event.apply {
                            displayConnection(item)
                            viewModel.onConnectBt(socket, item.actions)
                        }
                    }
                    is ConnectedEventType.Fail -> {
                        Toast.makeText(
                            requireContext(), getString(R.string.connection_failed_string), Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ConnectedEventType.WifiConnected -> {
                        event.apply {
                            displayConnection(item)
                            viewModel.onConnectWf(network, item.ssid, item.bssid)
                        }
                    }
                }
            }.collect()
        }
    }

    private fun displayConnection(item: ConnectionItem) {
        with(binding) {
            connectionCard.visibility = View.VISIBLE
            when (item) {
                is BtConnectionItem -> {
                    val img = ResourcesCompat.getDrawable(resources, R.drawable.bt_logo, null)
                    curConnectionIcon.setImageDrawable(img)
                    curConnectionSsidOrAddr.text = item.address
                }
                is WifiConnectionItem -> {
                    val img = ResourcesCompat.getDrawable(resources, R.drawable.wifi_logo, null)
                    curConnectionIcon.setImageDrawable(img)
                    curConnectionSsidOrAddr.text = item.ssid
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun initViews() {
        wifiManager.startScan()
        btManager.adapter.startDiscovery()
        binding.connectionsRv.adapter = adapter
        adapter.addItemClickListener(
            viewModel.getConnectionHandler()
        )
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(connectionsReceiver)
        super.onDestroy()
    }
}