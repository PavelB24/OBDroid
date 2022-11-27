package ru.barinov.obdroid.startFragment

import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.transition.doOnEnd
import com.google.android.material.button.MaterialButton
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding

object PermissionViewHelper {


    fun animateOnStart(){}

    fun animateRebase(){}


    private fun shouldHideLine(binding: PermissionsFragmentBinding): Boolean {
        binding.apply {
            return btIcon.visibility == View.GONE &&
                    locationIcon.visibility == View.GONE
                    || dozeIcon.visibility == View.GONE &&
                    fileSystemIcon.visibility == View.GONE
        }
    }

    private fun hideLine(binding: PermissionsFragmentBinding) {
        if (shouldHideLine(binding)) {
            Fade().apply {
                duration = 200
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
            btIcon.visibility = View.GONE
            btPermissionTitle.visibility = View.GONE
            btPermissionSwitch.visibility = View.GONE
        }
    }

    fun hideBtAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    btIcon.visibility = View.GONE
                    btPermissionTitle.visibility = View.GONE
                    btPermissionSwitch.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this)
                addTarget(binding.btIcon)
                addTarget(binding.btPermissionTitle)
                addTarget(binding.btPermissionSwitch)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            btIcon.visibility = View.INVISIBLE
            btPermissionTitle.visibility = View.INVISIBLE
            btPermissionSwitch.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    private fun tuneTransition(set: TransitionSet) {
        set.apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(Fade())
            duration = 800
            interpolator = AccelerateInterpolator()
        }
    }

    fun hideLocation(binding: PermissionsFragmentBinding) {
        binding.apply {
            locationIcon.visibility = View.GONE
            locationPermissionSwitch.visibility = View.GONE
            locationPermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideLocationAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    locationIcon.visibility = View.GONE
                    locationPermissionSwitch.visibility = View.GONE
                    locationPermissionTitle.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this)
                addTarget(binding.locationIcon)
                addTarget(binding.locationPermissionSwitch)
                addTarget(binding.locationPermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            locationIcon.visibility = View.INVISIBLE
            locationPermissionSwitch.visibility = View.INVISIBLE
            locationPermissionTitle.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    fun hideDoze(binding: PermissionsFragmentBinding) {
        binding.apply {
            dozeIcon.visibility = View.GONE
            dozePermissionSwitch.visibility = View.GONE
            dozePermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideDozeAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    dozeIcon.visibility = View.GONE
                    dozePermissionSwitch.visibility = View.GONE
                    dozePermissionTitle.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this)
                addTarget(binding.dozeIcon)
                addTarget(binding.dozePermissionSwitch)
                addTarget(binding.dozePermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            dozeIcon.visibility = View.INVISIBLE
            dozePermissionSwitch.visibility = View.INVISIBLE
            dozePermissionTitle.visibility = View.INVISIBLE
//            image.setVisibility(if (show) View.VISIBLE else View.GONE)
            hideLine(this)
        }
    }

    fun showButtonAnimate(root : ViewGroup, button : MaterialButton){
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
            fileSystemIcon.visibility = View.GONE
            fileSystemSwitch.visibility = View.GONE
            fileSystemPermissionTitle.visibility = View.GONE
            hideLine(this)
        }
    }

    fun hideExternalStorageAnimate(binding: PermissionsFragmentBinding) {
        binding.apply {
            TransitionSet().apply {
                doOnEnd {
                    fileSystemIcon.visibility = View.GONE
                    fileSystemPermissionTitle.visibility = View.GONE
                    fileSystemSwitch.visibility = View.GONE
                    hideLine(binding)
                }
                tuneTransition(this)
                addTarget(binding.fileSystemIcon)
                addTarget(binding.fileSystemSwitch)
                addTarget(binding.fileSystemPermissionTitle)
                TransitionManager.beginDelayedTransition(binding.mainGroup as ViewGroup, this)
            }
            fileSystemIcon.visibility = View.INVISIBLE
            fileSystemSwitch.visibility = View.INVISIBLE
            fileSystemPermissionTitle.visibility = View.INVISIBLE
        }
    }
}