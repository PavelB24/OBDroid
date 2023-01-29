package ru.barinov.obdroid.ui.connectionsFragment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.*
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.broadcastReceivers.ConnectionsBroadcastReceiver
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding
import ru.barinov.obdroid.ui.uiModels.BtItem
import ru.barinov.obdroid.utils.PermissionsChecker
import java.util.*

class ConnectionsFragment : Fragment() {


    companion object{
        const val bundleKey = "bk"
    }


    //События для flow все перенеси в ресивер, кроме поключения по WF  - там коллбэк, обрабатывай метод коннекта, пытаясь открыть полученный соккет

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding: ConnectionsLayoutBinding

    private lateinit var wifiManager: WifiManager

    private lateinit var btManager: BluetoothManager

    private val viewModel: ConnectionsViewModel by viewModel()

    private lateinit var btEnabler: BtEnabler

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
            connectionsToolbar.title = getString(R.string.connections_title)
        }
        wifiManager = requireContext().getSystemService(WifiManager::class.java)
        btManager = requireContext().getSystemService(BluetoothManager::class.java)
        btEnabler = BtEnabler(this, btManager)
        requireActivity().addMenuProvider(
            ConnectionsMenuProvider(
                btEnabler,
                requireContext()
            ), viewLifecycleOwner, Lifecycle.State.CREATED
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
        initViews()
        requireActivity().registerReceiver(connectionsReceiver,
            IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
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
        lifecycleScope.launchWhenResumed {
            viewModel.onConnectFlow.onEach { event ->
                when (event) {
                    is ConnectedEventType.BluetoothConnecting -> {
                        viewModel.removeConnectedBt(event.item)
                        event.apply {
                            displayConnection(item)
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
                        viewModel.removeConnectedWiFi(event.item)
                        event.apply {
                            if (findNavController().currentDestination?.id != R.id.wifiSettingsDialog){
                            findNavController().navigate(
                                R.id.action_connectionsFragment_to_wifiSettingsDialog
                            )
                        }
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
                    ConnectionReceiverEvent.AdapterStateChanged -> {
                        requireActivity().invalidateMenu()
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
        viewModel.handleBtDevice(device, true)
    }

    private fun onFailedBound(device: BluetoothDevice) {
        binding.connectionsRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        viewModel.handleBtDevice(device)
    }

    private fun showProgress() {
        binding.connectionsRv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun displayConnection(item: BtItem) {
        //todo(Change to progressbar dialog)
        if (findNavController().currentDestination?.id != R.id.onBtConnectionDialog) {
            findNavController().navigate(
                R.id.action_connectionsFragment_to_onBtConnectionDialog,
                Bundle().also {
                    it.putStringArray(bundleKey, arrayOf(item.name, item.address))
                }
            )
        }
//        AlertDialog.Builder(requireContext()).setTitle(
//            getString(R.string.bt_connected_title)
//        ).setCancelable(true).setMessage(
//            getString(
//                R.string.bt_connected_message,
//                if (item.name.isNullOrEmpty()) item.address else item.name
//            )
//        ).setPositiveButton(android.R.string.ok) { di, i ->
//            di.dismiss()
//        }
//            .show()
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

    private fun initViews() {
        binding.root.setOnRefreshListener {
            startBtDiscovery()
            wifiManager.startScan()
            binding.root.isRefreshing = false
        }
        binding.connectionsRv.adapter = adapter
        adapter.addItemClickListener(
            viewModel.getConnectionHandler()
        )
        btEnabler.askForBt()
        wifiManager.startScan()
    }


    private fun startBtDiscovery() {
        if (btManager.adapter?.isEnabled == true) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                btManager.adapter.startDiscovery()
            }
        }
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(connectionsReceiver)
        super.onDestroy()
    }
}