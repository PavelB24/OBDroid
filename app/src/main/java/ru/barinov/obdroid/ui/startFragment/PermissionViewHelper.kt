package ru.barinov.obdroid.ui.startFragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.annotation.MainThread
import androidx.core.transition.doOnEnd
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding

object PermissionViewHelper {

    @MainThread
    fun animateRebase(
        binding: PermissionsFragmentBinding,
        rebaseAction: () -> Unit
    ) {
        with(binding) {
            root.transitionToState(R.id.exit)
            CoroutineScope(Dispatchers.IO).launch {
                delay(1200)
                withContext(Dispatchers.Main){
                    rebaseAction.invoke()
                }
            }
        }

    }

    @MainThread
    fun animateLogo(anim: AnimatedVectorDrawable) {
        anim.start()
    }


    fun isLastCardGone(binding: PermissionsFragmentBinding): Boolean {
        binding.apply {
            return btPermissionSwitch.visibility == View.GONE
                    && locationPermissionSwitch.visibility == View.GONE
                    && fileSystemSwitch.visibility == View.GONE
                    && dozePermissionSwitch.visibility == View.GONE
        }
    }


    private fun shouldHideLine(binding: PermissionsFragmentBinding): Boolean {
        binding.apply {
            return btPermissionSwitch.visibility == View.GONE &&
                    locationPermissionSwitch.visibility == View.GONE
                    || dozePermissionSwitch.visibility == View.GONE &&
                    fileSystemSwitch.visibility == View.GONE
        }
    }

    private fun hideLine(binding: PermissionsFragmentBinding) {
        if (shouldHideLine(binding)) {
            Fade().apply {
                duration = 100
                addTarget(binding.line)
                doOnEnd {
                    binding.line.visibility = View.GONE
                }
                TransitionManager.beginDelayedTransition(binding.root, this)
                binding.line.visibility = View.INVISIBLE
            }
        }
    }

    fun hideBt(binding: PermissionsFragmentBinding) {
        binding.apply {
            btPermissionTitle.visibility = View.GONE
            btPermissionSwitch.visibility = View.GONE
        }
    }

    fun hideBtAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    btPermissionTitle.visibility = View.GONE
                    btPermissionSwitch.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this, Fade())
                addTarget(binding.btPermissionTitle)
                addTarget(binding.btPermissionSwitch)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            btPermissionTitle.visibility = View.INVISIBLE
            btPermissionSwitch.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    private fun tuneTransition(set: TransitionSet, transition: Transition) {
        set.apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(transition)
            duration = 800
            interpolator = AccelerateInterpolator()
        }
    }

    fun hideLocation(binding: PermissionsFragmentBinding) {
        binding.apply {
            locationPermissionSwitch.visibility = View.GONE
            locationPermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideLocationAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    locationPermissionSwitch.visibility = View.GONE
                    locationPermissionTitle.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this, Fade())
                addTarget(binding.locationPermissionSwitch)
                addTarget(binding.locationPermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            locationPermissionSwitch.visibility = View.INVISIBLE
            locationPermissionTitle.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    fun hideDoze(binding: PermissionsFragmentBinding) {
        binding.apply {
            dozePermissionSwitch.visibility = View.GONE
            dozePermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideDozeAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    dozePermissionSwitch.visibility = View.GONE
                    dozePermissionTitle.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this, Fade())
                addTarget(binding.dozePermissionSwitch)
                addTarget(binding.dozePermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            dozePermissionSwitch.visibility = View.INVISIBLE
            dozePermissionTitle.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    fun showButtonAnimate(root: ViewGroup, button: MaterialButton) {
        Fade().apply {
            interpolator = AccelerateInterpolator()
            addTarget(button)
            duration = 800
            TransitionManager.beginDelayedTransition(root, this)
        }
        button.visibility = View.VISIBLE
    }

    fun hideExternalStorage(binding: PermissionsFragmentBinding) {
        binding.apply {
            fileSystemSwitch.visibility = View.GONE
            fileSystemPermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideExternalStorageAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    fileSystemPermissionTitle.visibility = View.GONE
                    fileSystemSwitch.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this, Fade())
                addTarget(binding.fileSystemSwitch)
                addTarget(binding.fileSystemPermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            fileSystemSwitch.visibility = View.INVISIBLE
            fileSystemPermissionTitle.visibility = View.INVISIBLE
        }
    }
}