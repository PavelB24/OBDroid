package ru.barinov.obdroid.data

import android.content.Context
import android.content.res.AssetManager.ACCESS_STREAMING
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.domain.*
import java.security.KeyStore
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
        private const val EXTRA_ASSET_NAME = "commands_ext"
        private const val PROFILES_ASSET_NAME = "def_profiles"
        private const val AT_COMMANDS_ASSET_NAME = "at_commands"
    }

    private val commandsRepository: CommandsRepository by inject()
    private val troublesRepo: TroublesRepository by inject()
    private val profilesRepository: ProfilesRepository by inject()
    private val keyStore = KeyStore.getInstance("bks")


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val cipher = initCipher()
            loadMainCommands(cipher)
            loadAdditionalCommands(cipher)
            loadDiagnosticAtCommands(cipher)
            loadTroubleCodes(cipher)
            loadProfiles(cipher)
            Result.success()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Result.failure()
        }
    }

    private suspend fun loadDiagnosticAtCommands(cipher: Cipher) {
        val encryptedArray = context.assets.open(
            AT_COMMANDS_ASSET_NAME,
            ACCESS_STREAMING
        ).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        decrypted.inputStream().bufferedReader().use {
            val buffer = mutableListOf<AtCommandEntity>()
            while (it.ready()) {
                val lineContents = it.readLine().split("//")
                val isAccessible = lineContents[0] == "1"
                buffer.add(
                    AtCommandEntity(
                        lineContents[if (isAccessible) 3 else 2],
                        lineContents[if (isAccessible) 4 else 3],
                        if (isAccessible) lineContents[5] else null,
                        if (isAccessible) {
                            MeasurementUnit.values()[Integer.valueOf(lineContents[2])]
                        } else null,
                        isAccessible,
                        false,
                        CommandCategory.values()[Integer.valueOf(lineContents[1])]
                    )
                )
            }
            commandsRepository.populateWithCommands(buffer)
        }
    }

    private suspend fun loadProfiles(cipher: Cipher) {
        val encryptedArray = context.assets.open(PROFILES_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        decrypted.inputStream().bufferedReader().use {
            val buffer = mutableListOf<ProfileEntity>().also { list ->
                list.add(
                    ProfileEntity(
                        "Auto",
                        null,
                        null,
                        0,
                        isSelected = true,
                        isDeletable = false
                    )
                )
            }
            while (it.ready()) {
                val lineContents = it.readLine().split("//")
                buffer.add(
                    ProfileEntity(
                        lineContents[0],
                        lineContents[2],
                        if (lineContents.size == 4) lineContents[3] else null,
                        Integer.valueOf(lineContents[1]),
                        isSelected = false,
                        isDeletable = false
                    )
                )
            }
            profilesRepository.populateWithProfiles(buffer)
        }
    }

    private suspend fun loadTroubleCodes(cipher: Cipher) {
        val encryptedArray = context.assets.open(TROUBLES_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        decrypted.inputStream().bufferedReader().use {
            val buffer = mutableListOf<TroubleCodeEntity>()
            while (it.ready()) {
                val lineContents = it.readLine().split("//")
                val translate = if (lineContents.size == 3) lineContents.last() else null
                lineContents[0].apply {
                    val category: TroubleCodeType = when {
                        contains("X") -> TroubleCodeType.HEADER
                        take(3) == "P01" -> TroubleCodeType.FUEL_AND_AIR
                        take(3) == "P02" -> TroubleCodeType.FUEL_AND_AIR
                        take(3) == "P03" -> TroubleCodeType.IGNITION_SYSTEM
                        take(3) == "P04" -> TroubleCodeType.EMISSION_CONTROLS
                        take(3) == "P05" -> TroubleCodeType.SPEED_CONTROL_AUXILIARY
                        take(3) == "P06" -> TroubleCodeType.ECU_AND_AUXILIARY
                        take(3) == "P07" -> TroubleCodeType.CHASSIS
                        take(2) == "P1" -> TroubleCodeType.GM
                        else -> TroubleCodeType.COMMON
                    }
                    buffer.add(
                        TroubleCodeEntity(
                            lineContents[0],
                            lineContents[1],
                            translate,
                            category
                        )
                    )
                }
            }
            troublesRepo.populateWithTroubles(buffer)
        }
    }


    private suspend fun loadAdditionalCommands(cipher: Cipher) {
        val encryptedArray = context.assets.open(EXTRA_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        decrypted.inputStream().bufferedReader().use {
            val buffer = mutableListOf<CommandEntity>()
            while (it.ready()) {
                val lineContents = it.readLine().split("//")
                val translate = if (lineContents.size == 8) lineContents[6] else null
                val category: CommandCategory =
                    CommandCategory.values()[Integer.valueOf(lineContents[1])]
                buffer.add(
                    CommandEntity(
                        lineContents[3],
                        Integer.valueOf(lineContents[4]),
                        lineContents[2],
                        category,
                        lineContents[5],
                        translate,
                        isFav = false,
                        isCustomCommand = false,
                        getMeasurementUnit(lineContents, 1),
                        available = translate != null,
                        lineContents[0] == "1"
                    )
                )
            }
            commandsRepository.populateWithCommands(buffer)
        }
    }

    private fun getMeasurementUnit(lineContents: List<String>, padding: Int = 0): MeasurementUnit? {
        return if (lineContents.size == 7 + padding) {
            when (lineContents[6 + padding]) {
                "c" -> MeasurementUnit.CUSTOM
                "p" -> MeasurementUnit.PERCENT
                "t" -> MeasurementUnit.CELSIUS
                "kpa" -> MeasurementUnit.PRESSURE
                "rpm" -> MeasurementUnit.ROUND
                "kmh" -> MeasurementUnit.KILOMETRE_HOUR
                "ang" -> MeasurementUnit.ANGLE
                "gs" -> MeasurementUnit.GRAM_SEC
                "vc" -> MeasurementUnit.VOLTAGE_FUEL_TRIM
                "sec" -> MeasurementUnit.SEC
                "km" -> MeasurementUnit.KILOMETRE
                "v" -> MeasurementUnit.VOLT
                "co" -> MeasurementUnit.COUNT
                "ma" -> MeasurementUnit.MA
                "rat" -> MeasurementUnit.RATIO
                "min" -> MeasurementUnit.MINUTE
                "rvmakpa" -> MeasurementUnit.RATIO_V_MA_PRESSURE
                "lh" -> MeasurementUnit.LITRE_HOUR
                "nm" -> MeasurementUnit.NM
                "mgc" -> MeasurementUnit.MG_TO_CYLINDER
                "vin" -> MeasurementUnit.VIN
                "n" -> MeasurementUnit.NAME
                else -> null
            }
        } else null
    }

    private suspend fun loadMainCommands(cipher: Cipher) {
        val encryptedArray = context.assets.open(COMMANDS_ASSET_NAME, ACCESS_STREAMING).readBytes()
        val decrypted = cipher.doFinal(encryptedArray)
        decrypted.inputStream().bufferedReader().use {
            val buffer = mutableListOf<CommandEntity>()
            while (it.ready()) {
                val lineContents = it.readLine().split("//")
                val translate = if (lineContents.size == 7) lineContents[5] else null
                val category: CommandCategory =
                    CommandCategory.values()[Integer.valueOf(lineContents[1])]
                buffer.add(
                    CommandEntity(
                        lineContents[2],
                        Integer.valueOf(lineContents[3]),
                        "01",
                        category,
                        lineContents[4],
                        translate,
                        isFav = false,
                        isCustomCommand = false,
                        getMeasurementUnit(lineContents),
                        available = lineContents.size > 5,
                        lineContents[0] == "1"
                    )
                )
            }
            commandsRepository.populateWithCommands(buffer)
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