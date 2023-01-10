package ru.barinov.obdroid.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import ru.barinov.obdroid.data.CommandsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.barinov.obdroid.WifiConnectionSettingsViewModel
import ru.barinov.obdroid.ui.utils.ServiceCommander
import ru.barinov.obdroid.utils.ConnectionWatcher
import ru.barinov.obdroid.data.DataBase
import ru.barinov.obdroid.data.DbWorker
import ru.barinov.obdroid.data.TroublesRepository
import ru.barinov.obdroid.ui.activity.ActivityViewModel
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionActionHandler
import ru.barinov.obdroid.ui.connectionsFragment.ConnectionsViewModel
import ru.barinov.obdroid.preferences.Preferences
import ru.barinov.obdroid.ui.sensorsFragment.SensorsViewModel
import ru.barinov.obdroid.ui.settings.SettingsFragmentViewModel

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
        }).fallbackToDestructiveMigration().build()
    }

    single {
        androidApplication().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    factory {
        ConnectionActionHandler(androidContext(), get())
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

    viewModel {
        ConnectionsViewModel(get())
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