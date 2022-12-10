package ru.barinov.obdroid.di

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.WifiConnectionWatcher
import ru.barinov.obdroid.ui.activity.ActivityViewModel
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionActionHandler
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionsViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.settings.SettingsFragmentViewModel

private const val SHARED_PREFS_NAME = "prefs"


val mainModule = module {

    single {
        androidApplication().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    factory {
        ConnectionActionHandler(androidContext(), get())
    }


    single {
        ServiceCommander(androidContext())
    }


    single {
        Preferences(get())
    }

    single {
        WifiConnectionWatcher()
    }

    viewModel {
        ConnectionsViewModel(get())
    }

    viewModel {
        ActivityViewModel(get(), get())
    }

    viewModel{
        SettingsFragmentViewModel(get())
    }

}