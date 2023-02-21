package ru.barinov.obdroid.ui.troublesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.HomeFragment
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.TroubleCodesLayoutBinding

class TroubleHistory : Fragment() {


    private lateinit var binding: TroubleCodesLayoutBinding

    private val adapter by lazy { TroublesPagingAdapter() }

    private val viewModel: TroubleHistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TroubleCodesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun subscribe() {

    }

    private fun initViews() {
        (requireParentFragment().requireParentFragment() as HomeFragment).hideBottomNav()
        binding.apply {
            bottomHistoryKbPanel.visibility = View.VISIBLE
            clearCodesButton.visibility = View.GONE
            bottomHistoryKbPanel.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.troubles_history_item -> {
                        viewModel.changePageType(TroublePageType.DETECTED)
                    }
                    R.id.knowledge_base_item -> {
                        viewModel.changePageType(TroublePageType.ALL_KNOWN)
                    }
                }
                true
            }
            bottomHistoryKbPanel.selectedItemId = R.id.troubles_history_item
        }
    }


    override fun onDestroy() {
        (requireParentFragment().requireParentFragment() as HomeFragment).showBottomNav()
        super.onDestroy()
    }
}