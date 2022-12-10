package ru.barinov.obdroid

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.HomeLayoutBinding


class HomeFragment : Fragment() {

    private lateinit var binding : HomeLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = view.findViewById<View>(R.id.container2)
        val activity = requireActivity() as MainActivity
        binding.mainBottomNav.setupWithNavController(Navigation.findNavController(host))
        binding.toolbar.setupWithNavController(Navigation.findNavController(host), activity.getDrawer())
    }

}