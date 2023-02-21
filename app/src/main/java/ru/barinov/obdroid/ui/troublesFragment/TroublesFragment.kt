package ru.barinov.obdroid.ui.troublesFragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.TroubleCodesLayoutBinding

class TroublesFragment : Fragment() {

    private lateinit var binding: TroubleCodesLayoutBinding

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
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.troubles_toolbar_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.recent_troubles -> {
                            findNavController().navigate(
                                R.id.action_diagnosticFragment_to_troubleHistory
                            )
                        }
                    }
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.STARTED
        )
    }
}