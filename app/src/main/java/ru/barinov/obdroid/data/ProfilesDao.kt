package ru.barinov.obdroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.barinov.obdroid.domain.ProfileEntity

@Dao
interface ProfilesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun populateWithProfiles(profiles: List<ProfileEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles")
    fun getProfiles(): Flow<List<ProfileEntity>>

    @Transaction
    suspend fun selectProfile(oldSelectedName: String, newSelectedName: String){
        clearSelected(oldSelectedName)
        select(newSelectedName)
    }

    @Query("UPDATE profiles SET is_selected = 0 WHERE name =:oldSelectedName")
    suspend fun clearSelected(oldSelectedName: String)

    @Query("UPDATE profiles SET is_selected = 1 WHERE name =:newSelectedName")
    suspend fun select(newSelectedName: String)

    @Query("DELETE FROM profiles WHERE name =:name AND is_deletable = 0")
    suspend fun deleteProfileByName(name: String)

    @Query("UPDATE profiles SET protocol =:ordinal WHERE name == 'Auto'")
    suspend fun updateDefaultProtocol(ordinal: Int)

    @Query("SELECT * FROM profiles WHERE is_selected == 1")
    fun getSelectedProfile(): ProfileEntity
}