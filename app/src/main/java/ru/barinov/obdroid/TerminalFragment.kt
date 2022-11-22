package ru.barinov.obdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.databinding.TerminalFragmentBinding

class TerminalFragment : Fragment() {

    private lateinit var binding : TerminalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TerminalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}