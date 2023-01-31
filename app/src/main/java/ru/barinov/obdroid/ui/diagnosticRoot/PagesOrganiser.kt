package ru.barinov.obdroid.ui.diagnosticRoot

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.MeasurementUnit
import ru.barinov.obdroid.ui.diagnosticRoot.pages.GraphicFragment
import ru.barinov.obdroid.ui.diagnosticRoot.pages.SimpleViewFragment
import ru.barinov.obdroid.ui.diagnosticRoot.pages.WidgetFragment
import ru.barinov.obdroid.ui.uiModels.CommandDataContainer

class PagesOrganiser(
    val args: CommandDataContainer
) {
    companion object {
        const val commandArgsKey = "comKey"
    }

    fun getPageList(): List<Fragment> {
        return if (!args.isDynamic) {
            listOf(SimpleViewFragment().also { it.arguments = setArgs() })
        } else {
            listOf(SimpleViewFragment(), GraphicFragment(), WidgetFragment()).onEach {
                it.arguments = setArgs()
            }
        }
    }

    private fun setArgs(): Bundle {
        return Bundle().also {
            it.putParcelable(
                commandArgsKey,
                args
            )
        }
    }
}