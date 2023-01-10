package ru.barinov.obdroid.ui.sensorsFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.HomeFragment
import ru.barinov.obdroid.R
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.databinding.SensorsFragmentBinding
import ru.barinov.obdroid.domain.CommandActions
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.ui.utils.SensorsAdapter

class SensorsFragment : Fragment() {

    //toolbar
    private lateinit var binding: SensorsFragmentBinding
    private val popUpHandler by lazy { PopUpMenuHandler(requireContext()) }
    private val adapter by lazy { SensorsAdapter() }
    private val viewModel: SensorsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SensorsFragmentBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenStarted {
            (parentFragment?.parentFragment as HomeFragment).getToolbar().addMenuProvider(
                SensorsFragmentMenuProvider(viewModel),
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.commandsFlow.collectLatest {
                adapter.updateList(it)
            }
        }
    }

    private fun initViews() {
        adapter.bindActions(getActions())
        binding.sensorsRecycler.adapter = adapter
    }

    private fun getActions(): CommandActions {
        return object : CommandActions {
            override fun onLongClick(item: Command, itemView: View) {
                popUpHandler.buildPopUp(item, itemView){isFav, section, command ->
                    viewModel.handleFav(isFav, section, command)
                }
            }

            override fun onClick(item: Command) {
//                findNavController().navigate()
            }
        }
    }


}