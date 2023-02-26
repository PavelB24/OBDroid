package ru.barinov.obdroid

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.barinov.obdroid.ui.activity.MainActivity
import ru.barinov.obdroid.databinding.HomeLayoutBinding


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

        binding.root.setTransitionListener(object: MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
//                if (Navigation.findNavController(host).currentDestination?.id != R.id.diagnosticFragment &&
//                Navigation.findNavController(host).currentDestination?.id != R.id.mainFragment) {
                    if (currentId == R.id.normalBottom) {
                        (requireActivity() as MainActivity).showToolbarOnScroll()
                    } else {
                        (requireActivity() as MainActivity).hideToolbarOnScroll()
                    }
//                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
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