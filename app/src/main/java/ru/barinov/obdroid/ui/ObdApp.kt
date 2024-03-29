package ru.barinov.obdroid.ui

import android.app.Application
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.barinov.obdroid.data.DataBase
import ru.barinov.obdroid.di.mainModule

class ObdApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ObdApp)
            modules(mainModule)
        }
        Thread { runCatching {
            getKoin().get<DataBase>().getCommandsDao().count() }
        }.start()
    }
}