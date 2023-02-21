package ru.barinov.obdroid.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.barinov.obdroid.WifiConnectionSettingsViewModel
import ru.barinov.obdroid.core.ObdController
import ru.barinov.obdroid.data.*
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.utils.ConnectionWatcher
import ru.barinov.obdroid.ui.activity.ActivityViewModel
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionHandler
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionsViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.ShellViewModel
import ru.barinov.obdroid.ui.profilesListFragment.ProfilesViewModel
import ru.barinov.obdroid.ui.sensorsFragment.SensorsViewModel
import ru.barinov.obdroid.ui.settings.SettingsFragmentViewModel
import ru.barinov.obdroid.ui.startFragment.PermissionsFragmentViewModel
import ru.barinov.obdroid.ui.troublesFragment.TroubleHistoryViewModel

private const val SHARED_PREFS_NAME = "prefs"
private const val DATABASE_NAME = "obd_droid_db"


val mainModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            DataBase::class.java,
            DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(
                db: SupportSQLiteDatabase
            ) {
                super.onCreate(db)
                val prepopulateWork = OneTimeWorkRequestBuilder<DbWorker>().build()
                WorkManager.getInstance(androidApplication()).enqueue(prepopulateWork)
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                val prepopulateWork = OneTimeWorkRequestBuilder<DbWorker>().build()
                WorkManager.getInstance(androidApplication()).enqueue(prepopulateWork)
            }
        }).fallbackToDestructiveMigration().build()
    }

    single {
        androidApplication().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    factory {
        ConnectionHandler(androidContext(), get(), get())
    }

    single {
        ObdController(get(), get())
    }


    single {
        TroublesRepository(
            get<DataBase>().getTroublesDao()
        )
    }

    single {
        WifiConnectionSettingsViewModel(get(), get(), get())
    }


    single {
        ServiceCommander(androidContext())
    }

    single {
        CommandsRepository(
            get<DataBase>().getCommandsDao()
        )
    }

    single {
        Preferences(get())
    }

    single {
        ConnectionWatcher()
    }

    single {
        ProfilesRepository(get<DataBase>().getProfilesDao())
    }

    viewModel {
        ProfilesViewModel(get())
    }

    viewModel {
        PermissionsFragmentViewModel()
    }

    viewModel {
        ConnectionsViewModel(get())
    }

    viewModel {
        ShellViewModel(get())
    }

    viewModel {
        TroubleHistoryViewModel(get(), get())
    }

    viewModel {
        ActivityViewModel(get(), get())
    }

    viewModel {
        SensorsViewModel(get(), get())
    }

    viewModel {
        SettingsFragmentViewModel(get())
    }

}