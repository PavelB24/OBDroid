package ru.barinov.obdroid.data


import ru.barinov.obdroid.domain.ProfileEntity

class ProfilesRepository(
    private val dao: ProfilesDao
) {

    companion object{
        const val AUTO_PROFILE_NAME = "Auto"
    }

    suspend fun populateWithProfiles(profiles: List<ProfileEntity>) =
        dao.populateWithProfiles(profiles)

    suspend fun addProfile(profile: ProfileEntity) =
        dao.addProfile(profile)

    suspend fun deleteProfileByName(name: String) =
        dao.deleteProfileByName(name)


    suspend fun getSelectedProfile() = dao.getSelectedProfile()


    fun getProfiles() = dao.getProfiles()

    suspend fun updateDefaultProto(ordinal: Int) = dao.updateDefaultProtocol(ordinal)

    suspend fun selectNewProfile(oldName: String, newName: String) = dao.selectProfile(oldName, newName)
}