package ru.barinov.obdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding

class PermissionsFragment : Fragment() {

    private lateinit var binding : PermissionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PermissionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val graph =  findNavController().navInflater.inflate(R.navigation.app_navigation)

        graph.setStartDestination(R.id.homeFragment)

        findNavController().graph = graph
    }
}