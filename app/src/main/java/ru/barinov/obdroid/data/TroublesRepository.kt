package ru.barinov.obdroid.data

import ru.barinov.obdroid.domain.TroubleCodeEntity

class TroublesRepository(private val dao: TroublesDao) {

    suspend fun insertCode(entity: TroubleCodeEntity) = dao.insertTrouble(entity)
}