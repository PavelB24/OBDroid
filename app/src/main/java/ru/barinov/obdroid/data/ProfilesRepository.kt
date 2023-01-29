package ru.barinov.obdroid.data


import ru.barinov.obdroid.domain.ProfileEntity

class ProfilesRepository(
    private val dao: ProfilesDao
) {

    suspend fun populateWithProfiles(profiles: List<ProfileEntity>) =
        dao.populateWithProfiles(profiles)

    suspend fun addProfile(profile: ProfileEntity) =
        dao.addProfile(profile)

    suspend fun deleteProfileByName(name: String) =
        dao.deleteProfileByName(name)


    fun getProfiles() = dao.getProfiles()

    suspend fun updateDefaultProto(ordinal: Int) = dao.updateDefaultProtocol(ordinal)

    suspend fun selectNewProfile(oldName: String, newName: String) = dao.selectProfile(oldName, newName)
}