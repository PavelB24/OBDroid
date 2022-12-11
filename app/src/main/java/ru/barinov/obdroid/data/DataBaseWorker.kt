package ru.barinov.obdroid.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class DataBaseWorker(
    private val context: Context,
    private val parameters: WorkerParameters
) : CoroutineWorker(context, parameters), KoinComponent {



    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}