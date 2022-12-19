package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.barinov.obdroid.domain.TroubleCodeEntity

@Dao
interface TroublesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrouble(entity: TroubleCodeEntity)
}