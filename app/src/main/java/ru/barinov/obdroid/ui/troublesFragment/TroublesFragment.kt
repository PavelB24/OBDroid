package ru.barinov.obdroid.ui.troublesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import ru.barinov.obdroid.databinding.TroublesFragmentLayoutBinding

class TroublesFragment : Fragment() {

    private lateinit var binding: TroublesFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TroublesFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(
            TroublesMenuProvider(findNavController()),
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
    }
}