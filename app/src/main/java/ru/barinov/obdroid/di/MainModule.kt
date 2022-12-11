package ru.barinov.obdroid.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.WifiConnectionWatcher
import ru.barinov.obdroid.data.DataBase
import ru.barinov.obdroid.ui.activity.ActivityViewModel
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionActionHandler
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionsViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.settings.SettingsFragmentViewModel

private const val SHARED_PREFS_NAME = "prefs"
private const val DATABASE_NAME = "obd_droid_dm"


val mainModule = module {


    single {
        Room.databaseBuilder(
            androidContext(),
            DataBase::class.java,
            DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

            }
        }).fallbackToDestructiveMigration().build()
    }

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