package ru.barinov.obdroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.databinding.MainFragmentBinding

class DashFragment : Fragment() {

    private lateinit var binding : MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val first = layoutInflater.inflate(R.layout.e, FrameLayout(requireContext()))
//        binding.speedometerView.doOnPreDraw {
//            Log.d("@@@", "${it.x} ${it.y}")
//            val target = com.takusemba.spotlight.Target.Builder()
//                .setAnchor(binding.test)
//                .setShape(Circle(100f))
//                .setEffect(RippleEffect(100f, 200f, argb(30, 124, 255, 90)))
//                .setOverlay(first)
//                .build()
//            val spt = Spotlight.Builder(requireActivity())
//                .setTargets(target)
//                .setBackgroundColorRes(R.color.blue)
//                .setDuration(5000L)
//            .setContainer(binding.root)
//                .setAnimation(DecelerateInterpolator(2f))
//                .setOnSpotlightListener(object : OnSpotlightListener {
//                    override fun onStarted() {
//                    }
//
//                    override fun onEnded() {
//                    }
//                })
//                .build().start()
//        }
    }
}