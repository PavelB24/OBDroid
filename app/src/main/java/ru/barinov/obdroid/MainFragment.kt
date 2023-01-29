package ru.barinov.obdroid

import android.graphics.Color.argb
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.Circle
import ru.barinov.obdroid.databinding.MainFragmentBinding

class MainFragment : Fragment() {

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