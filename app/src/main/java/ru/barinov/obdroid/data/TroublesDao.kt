package ru.barinov.obdroid.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.barinov.obdroid.domain.TroubleCodeEntity

@Dao
interface TroublesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrouble(entity: TroubleCodeEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun prepopulateWithTroubles(troubles: List<TroubleCodeEntity>)

    @Query("SELECT * FROM trouble_codes WHERE is_history == 0")
    fun getAllKnownTroublesAsFlow(): Flow<List<TroubleCodeEntity>>


    @Query("SELECT * FROM trouble_codes WHERE is_history == 0")
    fun getAllKnownTroubles(): List<TroubleCodeEntity>

    @Query("SELECT * FROM trouble_codes WHERE is_history == 0 AND type =:typeOrdinal  LIMIT :limit OFFSET :offset")
    fun getAllKnownTroublesByType(
        limit: Int,
        offset: Int,
        typeOrdinal: Int,
        searchBy: String
    ): List<TroubleCodeEntity>


    @Query("SELECT * FROM trouble_codes WHERE is_history == 1 AND detection_time >=:timeBarrier  LIMIT :limit OFFSET :offset")
    fun getHistoryTroublesByTime(
        limit: Int,
        offset: Int,
        timeBarrier: Long,
        searchBy: String
    ): List<TroubleCodeEntity>

    @Query("SELECT * FROM trouble_codes WHERE is_history == 1 AND detection_time >=:timeBarrier AND type =:catOrdinal")
    fun getHistoryTroublesByTimeAndCategory(
        timeBarrier: Long,
        catOrdinal: Int
    ): Flow<List<TroubleCodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToHistory(entity: TroubleCodeEntity)


}