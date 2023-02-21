package ru.barinov.obdroid.ui.connectionsFragment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.*
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.broadcastReceivers.ConnectionsBroadcastReceiver
import ru.barinov.obdroid.core.ObdBus
import ru.barinov.obdroid.databinding.ConnectionsLayoutBinding
import ru.barinov.obdroid.ui.utils.CommonDialogUtil
import ru.barinov.obdroid.utils.PermissionsChecker
import java.util.*

class ConnectionsFragment : Fragment() {


    companion object {
        const val bundleKey = "bk"
    }


    //События для flow все перенеси в ресивер, кроме поключения по WF  - там коллбэк, обрабатывай метод коннекта, пытаясь открыть полученный соккет

    private val adapter by lazy { ConnectionsAdapter() }

    private lateinit var binding: ConnectionsLayoutBinding

    private lateinit var wifiManager: WifiManager

    private val viewModel: ConnectionsViewModel by viewModel()

    private lateinit var btHelper: BtHelper

    private val connectionsReceiver by lazy { ConnectionsBroadcastReceiver() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConnectionsLayoutBinding.inflate(inflater, container, false)
//        binding.apply {
//            val activity = requireActivity() as MainActivity
//            connectionsToolbar.title = getString(R.string.connections_title)
//            (requireActivity() as MainActivity).setSupportActionBar(connectionsToolbar)
//            connectionsToolbar.setupWithNavController(
//                findNavController(),
//                activity.getDrawer()
//            )
//        }
        wifiManager = requireContext().getSystemService(WifiManager::class.java)
        btHelper = BtHelper(this, requireContext().getSystemService(BluetoothManager::class.java))
        requireActivity().addMenuProvider(
                ConnectionsMenuProvider(
                    btHelper,
                    requireContext(),
                    object : SimpleConnectionI {
                        override fun onBtSelected(address: String, socket: BluetoothSocket) {
                            showProgress()
                            viewModel.connectBtDirectly(address, socket)
                        }

                        override fun onWiFiSelected() {
                            showProgress()
                            findNavController().navigate(
                                R.id.action_connectionsFragment_to_wifiSettingsDialog,
                                Bundle().also {
                                    it.putBoolean(WifiSettingsDialog.setUpKey, true)
                                }
                            )
                        }

                    }
                ), viewLifecycleOwner, Lifecycle.State.RESUMED
            )
        requireActivity().title = requireContext().getString(R.string.connections_title)
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
            viewModel.eventBusFlow.collectLatest {
                when (it) {
                    is ObdBus.ObdEvents.SuccessConnect -> {
                        hideProgress()
                        //with animations
                        rebaseAfterConnect()
                    }
                    is ObdBus.ObdEvents.ConnectionFailed -> {
                        hideProgress()
                        CommonDialogUtil.showCantOrFailConnectDialog(requireContext())
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.onConnectFlow.onEach { event ->
                when (event) {
                    is ConnectedEventType.BluetoothConnecting -> {
                        viewModel.removeConnectedBt(event.item)
                        event.apply {
                            displayConnection()
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
                            if (findNavController().currentDestination?.id != R.id.wifiSettingsDialog) {
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
                        event.apply {
                            onDeviceBounded(device, rssi, clazz)
                        }
                    }
                    is ConnectionReceiverEvent.BoundFailed -> {
                        event.apply {
                            onFailedBound(device, rssi, clazz)
                        }
                    }
                    is ConnectionReceiverEvent.BoundingStarted -> {
                        showProgress()
                    }
                    is ConnectionReceiverEvent.NewBtDeviceFound -> {
                        event.apply {
                            onNewBtDevice(device, rssi, clazz)
                        }
                    }
                    is ConnectionReceiverEvent.ScanAvailable -> {
                        doOnNewScanResults()
                    }
                    is ConnectionReceiverEvent.UnBounded -> {
                        event.apply {
                            onNewBtDevice(device, rssi, clazz)
                        }
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

    private fun rebaseAfterConnect() {

    }

    private fun onDeviceBounded(
        device: BluetoothDevice,
        rssi: Short?,
        clazz: BluetoothClass?
    ) {
        hideProgress()
        viewModel.handleBtDevice(
            device,
            rssi,
            clazz,
            true
        )
    }

    private fun onFailedBound(
        device: BluetoothDevice,
        rssi: Short?,
        clazz: BluetoothClass?
    ) {
        hideProgress()
        viewModel.handleBtDevice(
            device,
            rssi,
            clazz
        )
    }

    private fun hideProgress() {
        binding.connectionsRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgress() {
        binding.connectionsRv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun displayConnection() {
        //todo(Change to progressbar dialog)
        showProgress()
    }


    @SuppressLint("MissingPermission")
    private fun doOnNewScanResults() {
        if (PermissionsChecker.hasLocationPermission(requireContext())) {
            viewModel.handleScanResults(wifiManager.scanResults)
        }
    }

    private fun onNewBtDevice(
        device: BluetoothDevice,
        rssi: Short?,
        clazz: BluetoothClass?
    ) {
        viewModel.handleBtDevice(
            device,
            rssi,
            clazz
        )
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
        btHelper.askForBt()
        wifiManager.startScan()
    }


    private fun startBtDiscovery() {
        if (btHelper.getAdapter()?.isEnabled == true) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                btHelper.getAdapter()?.startDiscovery()
            }
        }
    }

    override fun onDestroy() {
        try {
            requireActivity().unregisterReceiver(connectionsReceiver)
        } catch (e: Exception){
            e.printStackTrace()
        }
        super.onDestroy()
    }
}