package ru.barinov.obdroid.ui.sensorsFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.HomeFragment
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.base.ExtendedSearchListener
import ru.barinov.obdroid.base.ItemInteractor
import ru.barinov.obdroid.databinding.SensorsFragmentBinding
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.ui.utils.SensorsAdapter

class SensorsFragment : Fragment() {

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
        lifecycleScope.launchWhenCreated {
            requireActivity().addMenuProvider(
                SensorsFragmentMenuProvider(viewModel),
                viewLifecycleOwner,
                Lifecycle.State.STARTED
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

    private fun getActions(): ItemInteractor<Command> {
        return object : ItemInteractor<Command> {
            override fun onLongClick(item: Command, view: View) {
                popUpHandler.buildPopUp(item, view){ command, fav ->
                    viewModel.handleFav(command, fav)
                }
            }

            override fun onClick(item: Command) {
//                findNavController().navigate()
            }
        }
    }


}