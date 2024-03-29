package ru.barinov.obdroid.ui.troublesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.HomeFragment
import ru.barinov.obdroid.core.getGrandParent
import ru.barinov.obdroid.databinding.TroublesFragmentLayoutBinding

class TroubleHistory : Fragment() {


    private lateinit var binding: TroublesFragmentLayoutBinding

    private val adapter by lazy { TroublesPagingAdapter() }

    private val viewModel: TroubleHistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TroublesFragmentLayoutBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(
            TroubleHistoryMenuProvider(viewModel),
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
        getGrandParent<HomeFragment>()?.hideAndLockBottom()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.troubleCodesFlow.collectLatest {
                adapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collectLatest {
                binding.onEmptyTitle.visibility =
                    if (adapter.itemCount > 0) View.GONE else View.VISIBLE
            }
        }
    }

    private fun initViews() {
        //to toolbar
        binding.apply {
            detectedTroublesRw.adapter = adapter
            clearCodesButton.visibility = View.GONE
//            bottomHistoryKbPanel.setOnItemSelectedListener {
//                when (it.itemId) {
//                    R.id.troubles_history_item -> {
//                        viewModel.changePageType(TroublePageType.DETECTED)
//                    }
//                    R.id.knowledge_base_item -> {
//                        viewModel.changePageType(TroublePageType.ALL_KNOWN)
//                    }
//                }
//                true
//            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getGrandParent<HomeFragment>()?.showAndUnLockBottom()
    }
}