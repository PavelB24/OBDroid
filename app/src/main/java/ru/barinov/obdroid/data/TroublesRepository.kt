package ru.barinov.obdroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.barinov.obdroid.domain.TroubleCodeEntity
import ru.barinov.obdroid.domain.TroubleCodeType
import ru.barinov.obdroid.ui.troublesFragment.TroublesPagingSource
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroublesRepository(private val dao: TroublesDao) {

    suspend fun insertCode(entity: TroubleCodeEntity) = dao.insertTrouble(entity)

    suspend fun populateWithTroubles(troubles: List<TroubleCodeEntity>) =
        dao.prepopulateWithTroubles(troubles)


    suspend fun getHistoryTroubles(
        time: Long,
        searchBy: String,
        category: TroubleCodeType
    ): Flow<PagingData<TroubleCode>> {
        return Pager(
            config = PagingConfig(
                pageSize = TroublesPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = TroublesPagingSource.PAGE_SIZE
            ),
            pagingSourceFactory = {
                TroublesPagingSource(
                    dao = dao,
                    category = category,
                    searchBy = searchBy,
                    time = time
                )
            }
        ).flow
    }

    suspend fun getAllKnownTroubles() = dao.getAllKnownTroubles()

    fun getAllKnownTroublesByType(type: TroubleCodeType, searchBy: String): Flow<PagingData<TroubleCode>> {
        return Pager(
            config = PagingConfig(
                pageSize = TroublesPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = TroublesPagingSource.PAGE_SIZE
            ),
            pagingSourceFactory = {
                TroublesPagingSource(
                    dao = dao,
                    category = type,
                    searchBy = searchBy,
                )
            }
        ).flow
    }



}