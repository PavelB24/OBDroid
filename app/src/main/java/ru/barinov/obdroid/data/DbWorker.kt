package ru.barinov.obdroid.data

import android.content.Context
import android.content.res.AssetManager.ACCESS_STREAMING
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.CommandsRepository
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.domain.CommandCategory
import ru.barinov.obdroid.domain.CommandEntity
import ru.barinov.obdroid.domain.TroubleCodeEntity
import ru.barinov.obdroid.domain.TroubleCodeType
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DbWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private companion object {
        private const val ALGORITHM = "AES"
        private const val COMMANDS_ASSET_NAME = "commands"
        private const val TROUBLES_ASSET_NAME = "trouble_codes"
        private const val EXTRA_ASSET_NAME = "extra"
    }

    private val commandsRepo: CommandsRepository by inject()
    private val troublesRepo: TroublesRepository by inject()
    private val keyStore = KeyStore.getInstance("bks")


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val cipher = initCipher()
            loadMainCommands(cipher)
            loadOtherCommands(cipher)
            loadTroubleCodes(cipher)
            Result.success()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Result.failure()
        }

    }

    private suspend fun loadTroubleCodes(cipher: Cipher) {
        val encryptedArray = context.assets.open(TROUBLES_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        val reader = decrypted.inputStream().bufferedReader()
        while (reader.ready()) {
            val lineContents = reader.readLine().split("//")
            val translate = if (lineContents.size == 3) lineContents.last() else null
            val category: TroubleCodeType = when {
                lineContents[0].contains("X") -> TroubleCodeType.HEADER
                lineContents[0].take(3) == "P01" -> TroubleCodeType.FUEL_AND_AIR
                lineContents[0].take(3) == "P02" -> TroubleCodeType.FUEL_AND_AIR
                lineContents[0].take(3) == "P03" -> TroubleCodeType.IGNITION_SYSTEM
                lineContents[0].take(3) == "P04" -> TroubleCodeType.EMISSION_CONTROLS
                lineContents[0].take(3) == "P05" -> TroubleCodeType.SPEED_CONTROL_AUXILIARY
                lineContents[0].take(3) == "P06" -> TroubleCodeType.ECU_AND_AUXILIARY
                lineContents[0].take(3) == "P07" -> TroubleCodeType.CHASSIS
                lineContents[0].take(2) == "P1" ->  TroubleCodeType.GM
                else -> TroubleCodeType.COMMON
            }

            troublesRepo.insertCode(
                TroubleCodeEntity(
                    lineContents[0],
                    lineContents[1],
                    translate,
                    category
                )
            )
        }
    }

    private suspend fun loadOtherCommands(cipher: Cipher) {
        val encryptedArray = context.assets.open(EXTRA_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        val reader = decrypted.inputStream().bufferedReader()
        while (reader.ready()) {
            val lineContents = reader.readLine().split("//")
            val translate = if (lineContents.size == 5) lineContents.last() else null
            val category: CommandCategory =
                if(lineContents[3].contains("supported", true))
                    CommandCategory.SUPPORTED_PIDS else CommandCategory.VEHICLE_INFO
            commandsRepo.insertCommand(
                CommandEntity(
                    lineContents[1],
                    Integer.valueOf(lineContents[2]),
                    lineContents[0],
                    category,
                    lineContents[3],
                    translate,
                    isChosen = false,
                    isCustomCommand = false
                )
            )
        }
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