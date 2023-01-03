package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import ru.barinov.obdroid.domain.TroubleCodeEntity

@Dao
interface TroublesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrouble(entity: TroubleCodeEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun prepopulateWithTroubles(troubles: List<TroubleCodeEntity>)
}