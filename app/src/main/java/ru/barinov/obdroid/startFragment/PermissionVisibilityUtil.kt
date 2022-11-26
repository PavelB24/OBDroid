package ru.barinov.obdroid.startFragment

import android.view.View
import ru.barinov.obdroid.databinding.PermissionsFragmentBinding

object PermissionVisibilityUtil {

    fun hideBt(binding: PermissionsFragmentBinding){
        binding.apply {
            btIcon.visibility = View.GONE
            btPermissionSwitch.visibility = View.GONE
            btPermissionTitle.visibility = View.GONE
        }
    }

    fun hideLocation(binding: PermissionsFragmentBinding){
        binding.apply {
            locationIcon.visibility = View.GONE
            locationPermissionSwitch.visibility = View.GONE
            locationPermissionTitle.visibility = View.GONE
        }
    }

    fun hideDoze(binding: PermissionsFragmentBinding){
        binding.apply {
            dozeIcon.visibility = View.GONE
            dozePermissionSwitch.visibility = View.GONE
            dozePermissionTitle.visibility = View.GONE
        }
    }

    fun hideExternalStorage(binding: PermissionsFragmentBinding){
        binding.apply {
            fileSystemIcon.visibility = View.GONE
            fileSystemSwitch.visibility = View.GONE
            fileSystemPermissionTitle.visibility = View.GONE
        }
    }
}