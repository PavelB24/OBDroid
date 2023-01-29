package ru.barinov.obdroid.ui.diagnosticRoot

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit
import ru.barinov.obdroid.ui.diagnosticRoot.pages.GraphicFragment
import ru.barinov.obdroid.ui.diagnosticRoot.pages.SimpleViewFragment
import ru.barinov.obdroid.ui.diagnosticRoot.pages.WidgetFragment

class PagesOrganiser(
    private val commandHex: String,
    private val isDynamic: Boolean,
    private val measurementUnit: MeasurementUnit,
    private val commandCategory: CommandCategory
) {

    fun getPageList(): List<Fragment> {
        return if (!isDynamic) {
             listOf(SimpleViewFragment().also { it.arguments = setArgs() })
        } else {
            listOf(SimpleViewFragment(), GraphicFragment(), WidgetFragment()).also { 
                it.forEach { 
                    it.arguments = setArgs()
                }
            }
        } 
    }

    private fun setArgs(): Bundle {

    }
}