package ru.barinov.obdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.databinding.ShellFragmentLayoutBinding
import ru.barinov.obdroid.ui.ShellViewModel

class ShellFragment : Fragment() {

    private lateinit var binding : ShellFragmentLayoutBinding
    private val viewModel: ShellViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShellFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
//            viewModel.rawDataFlow.onEach {
//
//            }.collect()
        }
    }

    private fun initViews() {
        binding.root.background = ResourcesCompat.getDrawable(
            resources,
            viewModel.getSavedBackgroundId(),
            null
        )
        binding.sendButton.setOnClickListener {  }

    }


//
//    private fun fillString(bytes: ByteArray) {
//        Log.d("@@@", "FILLDEX")
//        val dexInput = binding.stringInput
//        val date = DateFormat.getDateTimeInstance(
//            DateFormat.SHORT, DateFormat.SHORT
//        ).format(System.currentTimeMillis())
//        if (dexInput.text.isEmpty()) {
//            dexInput.text = "$date: \n ${bytes.decodeToString()}"
//        } else {
//            dexInput.text = "${dexInput.text} \n $date: \n ${bytes.decodeToString()}"
//        }
//    }

//
//    @SuppressLint("MissingPermission")
//    private fun showMyDialog(devices: MutableSet<BluetoothDevice>, bt: BluetoothManager) {
//        val addresses = mutableListOf<String>()
//        val names = mutableListOf<String>()
//        if (devices.size > 0) {
//            for (device in devices) {
//                names.add("${device.name} \n ${device.address}")
//                addresses.add(device.address)
//            }
//        }
//
//        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
//
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
//            this,
//            R.layout.select_dialog_singlechoice,
//            names.toTypedArray()
//        )
//
//        alertDialog.setSingleChoiceItems(
//            adapter,
//            -1,
//            DialogInterface.OnClickListener { dialog, which ->
//                dialog.dismiss()
//                val position: Int = (dialog as AlertDialog).listView.checkedItemPosition
//                val deviceAddress: String = addresses[position]
//                this.deviceAddr = deviceAddress
//                deviceAddr?.let {
//                    val device = bt.adapter.getRemoteDevice(it)
//                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//                    val socket = device.createInsecureRfcommSocketToServiceRecord(uuid)
//                    try {
//                        socket.connect()
//                    } catch (e: Exception) {
//                        Toast.makeText(this, "CANT CONNECT", Toast.LENGTH_LONG).show()
//                    }
//                    source = BluetoothSource(socket)
//                    source?.let { source ->
//                        Log.d("@@@", "SOURCE")
//                        scope.launch {
//                            source.observeByteCommands(this)
//                        }
//                        binding.sendButton.setOnClickListener {
//                            Log.d("@@@", "ON SEND")
//                            lifecycleScope.launchWhenStarted {
//                                val text = "${binding.output.text}\r"
//                                source.outputByteFlow.emit(text.toByteArray(Charsets.US_ASCII))
//                            }
//                        }
//                        observeAnswers(source)
//                    }
//                }
//            })
//        alertDialog.setTitle("Select Bluetooth device")
//        alertDialog.show()
//    }
//


}