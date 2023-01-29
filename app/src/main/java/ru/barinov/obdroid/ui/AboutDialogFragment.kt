package ru.barinov.obdroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.R
import ru.barinov.obdroid.databinding.AboutFragmentLayoutBinding

class AboutDialogFragment: DialogFragment() {

    private lateinit var binding: AboutFragmentLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AboutFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message.text = getString(R.string.about_obd_droid_text, BuildConfig.VERSION_NAME)
        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}