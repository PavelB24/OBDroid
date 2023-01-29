package ru.barinov.obdroid.ui.diagnosticRoot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.barinov.obdroid.databinding.DiagnosticRootFragmentLayoutBinding

class DiagnosticRootFragment : Fragment()  {

    private lateinit var binding: DiagnosticRootFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}