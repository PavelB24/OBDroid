package ru.barinov.obdroid.ui.sensorsFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.barinov.obdroid.base.Command
import ru.barinov.obdroid.databinding.SensorsFragmentBinding
import ru.barinov.obdroid.domain.CommandActions
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
        Log.d("@@@", "Sens")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
               return true
            }
        }, viewLifecycleOwner)
        initViews()
        subscribe()
    }

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.commandsFlow.collectLatest {
                Log.d("@@@", it.size.toString())
                adapter.updateList(it)
            }
        }
    }

    private fun initViews() {
        adapter.bindActions(getActions())
        binding.sensorsRecycler.adapter = adapter
    }

    private fun getActions(): CommandActions {
        return object: CommandActions{
            override fun onLongClick(item: Command) {
                popUpHandler.buildPopUp(item)
            }

            override fun onClick(item: Command) {
//                findNavController().navigate()
            }
        }
    }


}