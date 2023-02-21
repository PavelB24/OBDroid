package ru.barinov.obdroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
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
        type: TroubleCodeType,
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
    suspend fun getAllKnownTroublesByType(type: TroubleCodeType, searchBy: String): Flow<PagingData<TroubleCode>> {
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