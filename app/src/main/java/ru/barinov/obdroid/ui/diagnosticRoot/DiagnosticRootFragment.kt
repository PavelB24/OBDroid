package ru.barinov.obdroid.ui.diagnosticRoot

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.DiagnosticRootFragmentLayoutBinding
import ru.barinov.obdroid.ui.diagnosticRoot.PagesOrganiser.Companion.commandArgsKey
import ru.barinov.obdroid.ui.diagnosticRoot.pages.GraphicFragment
import ru.barinov.obdroid.ui.diagnosticRoot.pages.SimpleViewFragment
import ru.barinov.obdroid.ui.uiModels.CommandDataContainer

class DiagnosticRootFragment : Fragment() {

    private lateinit var binding: DiagnosticRootFragmentLayoutBinding

    private var args: CommandDataContainer? = null

    private lateinit var adapter: ViewPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        args = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getParcelable(commandArgsKey, CommandDataContainer::class.java)
        else requireArguments().getParcelable(commandArgsKey) as? CommandDataContainer
        args ?: throw IllegalArgumentException("Args should be provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DiagnosticRootFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args?.let {
            val organiser = PagesOrganiser(it)
            adapter = ViewPagerAdapter(
                organiser.getPageList(),
                requireActivity().supportFragmentManager,
                lifecycle
            )
            binding.apply {
                viewPager.adapter = adapter
                TabLayoutMediator(tabsView, viewPager) { tab, position ->
                    val item = adapter.fragments[position]
                    tab.text = when (item) {
                        is GraphicFragment -> getString(R.string.graphic_fragment_title)
                        is SimpleViewFragment -> getString(R.string.simple_view_fragment_title)
                        else -> getString(R.string.widget_view_fragment_title)
                    }
                }
                    .attach()
            }
        }
    }
}