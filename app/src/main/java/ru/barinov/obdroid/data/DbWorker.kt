package ru.barinov.obdroid.data

import android.content.Context
import android.content.res.AssetManager.ACCESS_STREAMING
import android.os.Build
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.CommandsRepository
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.CommandEntity
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.KeyStore
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.SecretKeySpec

class DbWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private companion object {
        private const val ALGORITHM = "AES"
        private const val COMMANDS_ASSET_NAME = "commands"
        private const val troublesAssetName = ""
    }

    private val commandsRepo: CommandsRepository by inject()
    private val troublesRepo: TroublesRepository by inject()
    private val keyStore = KeyStore.getInstance("bks")


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val cipher = initCipher()
            loadMainCommands(cipher)
            loadOtherCommands()
            loadTroubleCodes()
            Result.success()
        } catch (e : Exception){
            if(BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Result.failure()
        }

    }

    private suspend fun loadTroubleCodes() {

    }

    private suspend fun loadOtherCommands() {

    }

    private suspend fun loadMainCommands(cipher: Cipher) {
        val encryptedArray = context.assets.open(COMMANDS_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        val reader = decrypted.inputStream().bufferedReader()
        while (reader.ready()) {
            val lineContents = reader.readLine().split("//")
            val translate = if (lineContents.size == 4) lineContents.last() else null
            val category: CommandCategory = when {
                lineContents[2].contains("diesel", true) -> CommandCategory.DIESEL
                lineContents[2].contains("hybrid", true) -> CommandCategory.HYBRID
                lineContents[2].contains("Oxygen", true) -> CommandCategory.OXYGEN_SENSOR
                lineContents[2].contains("PIDs supported") -> CommandCategory.SUPPORTED_PIDS
                else -> CommandCategory.COMMON
            }
            commandsRepo.insertCommand(
                CommandEntity(
                    lineContents[0],
                    Integer.valueOf(lineContents[1]),
                    "01",
                    category,
                    lineContents[2],
                    translate,
                    isChosen = false,
                    isCustomCommand = false
                )
            )
        }
    }

    private fun initCipher(): Cipher {
        keyStore.load(
            context.assets.open("bks_mainks_merge.jks", ACCESS_STREAMING),
            "decompose".toCharArray()
        )
        val k = keyStore.getKey("dbkey", "dbdecompose".toCharArray())
        val keySpec = SecretKeySpec(k.encoded, ALGORITHM)
        return Cipher.getInstance(ALGORITHM).also {
            it.init(Cipher.DECRYPT_MODE, keySpec)
        }
    }


}