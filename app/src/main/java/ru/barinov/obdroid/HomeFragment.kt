package ru.barinov.obdroid

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.HomeLayoutBinding
import ru.barinov.obdroid.databinding.PrefsLayoutBinding
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.sensorsFragment.SensorsFragment
import ru.barinov.obdroid.ui.startFragment.PermissionsFragmentViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: HomeLayoutBinding

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
        binding.mainBottomNav.setupWithNavController(Navigation.findNavController(host))
        lifecycleScope.launchWhenStarted {
            (requireActivity() as MainActivity).apply {
                hideAbout()
                getToolbar().setupWithNavController(
                    Navigation.findNavController(host),
                    getDrawer()
                )
            }
        }
    }


    fun hideBottomNav(){
        binding.mainBottomNav.visibility = View.GONE
    }

    fun showBottomNav(){
        binding.mainBottomNav.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).apply {
            showAbout()
            getToolbar().setupWithNavController(findNavController(), getDrawer())
        }
    }

}