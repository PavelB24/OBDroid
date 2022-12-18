package ru.barinov.obdroid.ui.connectionsFragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.*
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.base.ConnectionItem
import ru.barinov.obdroid.broadcastReceivers.ConnectionsBroadcastReceiver
import ru.barinov.obdroid.utils.StartPermissionsUtil
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding
import ru.barinov.obdroid.ui.uiModels.BtConnectionItem
import ru.barinov.obdroid.ui.uiModels.WifiConnectionItem
import ru.barinov.obdroid.utils.PermissionsChecker
import java.util.*

class ConnectionsFragment : Fragment() {


    //События для flow все перенеси в ресивер, кроме поключения по WF  - там коллбэк, обрабатывай метод коннекта, пытаясь открыть полученный соккет

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding: ConnectionsLayoutBinding

    private lateinit var wifiManager: WifiManager

    private lateinit var btManager: BluetoothManager

    private val viewModel by viewModel<ConnectionsViewModel>()

    private val connectionsReceiver by lazy { ConnectionsBroadcastReceiver() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConnectionsLayoutBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as MainActivity).setSupportActionBar(connectionsToolbar)
            connectionsToolbar.setupWithNavController(findNavController())
            connectionsToolbar.title = ""
        }
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
                            if(!btManager.adapter.isEnabled) {
                                btManager.adapter.enable()
                            }
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
            viewModel.onConnectFlow.onEach { event ->
                when (event) {
                    is ConnectedEventType.BluetoothConnected -> {
                        event.apply {
                            displayConnection(item)
                            viewModel.onConnectBt(socket, item.actions)
                        }
                    }
                    is ConnectedEventType.Fail -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.connection_failed_string),
                            Toast.LENGTH_SHORT
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
        lifecycleScope.launchWhenStarted {
            connectionsReceiver.receiverEvents.onEach { event ->
                when (event) {
                    is ConnectionReceiverEvent.BluetoothBounded -> {
                        onDeviceBounded(event.device)
                    }
                    is ConnectionReceiverEvent.BoundFailed -> {
                        onFailedBound(event.device)
                    }
                    is ConnectionReceiverEvent.BoundingStarted -> {
                        showProgress()
                    }
                    is ConnectionReceiverEvent.NewBtDeviceFound -> {
                        onNewBtDevice(event.device)
                    }
                    is ConnectionReceiverEvent.ScanAvailable -> {
                        doOnNewScanResults()
                    }
                    is ConnectionReceiverEvent.UnBounded -> {
                        onNewBtDevice(event.device)
                    }
                    is ConnectionReceiverEvent.ReBound -> {
                        //todo
                    }
                }
            }.collect()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.scanResult.onEach {
                adapter.newItems(it)
            }.collect()
        }
    }

    private fun onDeviceBounded(device: BluetoothDevice) {
        binding.connectionsRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        viewModel.apply {
            handleBtDevice(device)
            connectBounded(device)
        }
    }

    private fun onFailedBound(device: BluetoothDevice) {
        binding.connectionsRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        viewModel.handleBtDevice(device)
        Snackbar.make(requireView(), "Bound Failed", Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress() {
        binding.connectionsRv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun displayConnection(item: ConnectionItem) {
        with(binding) {
            connectionCard.visibility = View.VISIBLE
            when (item) {
                is BtConnectionItem -> {
                    val img = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bt_logo,
                        null
                    )
                    curConnectionIcon.setImageDrawable(img)
                    curConnectionSsidOrAddr.text = item.address
                }
                is WifiConnectionItem -> {
                    val img = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.wifi_logo,
                        null
                    )
                    curConnectionIcon.setImageDrawable(img)
                    curConnectionSsidOrAddr.text = item.ssid
                }
            }
        }

    }


    @SuppressLint("MissingPermission")
    private fun doOnNewScanResults() {
        if (PermissionsChecker.hasLocationPermission(requireContext())) {
            viewModel.handleScanResults(wifiManager.scanResults)
        }
    }

    private fun onNewBtDevice(device: BluetoothDevice) {
        viewModel.handleBtDevice(device)
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