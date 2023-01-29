package ru.barinov.obdroid.ui.connectionsFragment.bindedDialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.R
import ru.barinov.obdroid.core.ObdEventBus
import ru.barinov.obdroid.databinding.BtConnectionDialogLayoutBinding
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionsFragment

class OnBtConnectionDialog: DialogFragment() {

    private lateinit var binding: BtConnectionDialogLayoutBinding

    private val viewModel: OnConnectingDialogViewModel by viewModel()

    private var extras: Array<String>? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        extras = requireArguments().getStringArray(ConnectionsFragment.bundleKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BtConnectionDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun initViews() {
        binding.okButton.setOnClickListener {
            dismiss()
        }
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.onEach {
                when(it){
                    is ObdEventBus.ObdEvents.ConnectionFailed -> {}
                    ObdEventBus.ObdEvents.SuccessConnect -> {
                        extras?.let {
                            binding.progressBar.visibility = View.GONE
                            binding.message.setText(
                                getString(
                                    R.string.bt_connected_message,
                                    it[0].ifEmpty { it[1] }
                                )
                            )
                            lifecycleScope.launch(Dispatchers.IO) {
                                delay(500)
                                withContext(Dispatchers.Main){
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }.collect()
        }
    }

}