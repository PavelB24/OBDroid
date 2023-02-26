package ru.barinov.obdroid.ui.troublesFragment

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.barinov.obdroid.BuildConfig
import ru.barinov.obdroid.data.TroublesDao
import ru.barinov.obdroid.domain.TroubleCodeType
import ru.barinov.obdroid.domain.toTroubleCode
import ru.barinov.obdroid.ui.uiModels.TroubleCode

class TroublesPagingSource(
    private val dao: TroublesDao,
    private val category: TroubleCodeType,
    private val searchBy: String = "",
    private val time: Long? = null
) : PagingSource<Int, TroubleCode>() {


    companion object {
        const val PAGE_SIZE = 25
    }

    override fun getRefreshKey(state: PagingState<Int, TroubleCode>): Int? {
        val anchorPos = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPos) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TroubleCode> {

        val pageIndex = params.key ?: 0

        try {
            val pageData = when (time) {
                null -> {
                    if (category == TroubleCodeType.UNDEFINED) {
                        dao.getAllKnownTroubles(PAGE_SIZE, PAGE_SIZE * pageIndex, searchBy)
                            .map { it.toTroubleCode() }
                    } else {
                        dao.getAllKnownTroublesByType(
                            PAGE_SIZE, PAGE_SIZE * pageIndex, category.ordinal, searchBy
                        ).map { it.toTroubleCode() }
                    }
                }
                else -> {
                    dao.getHistoryTroublesByTime(
                        PAGE_SIZE, PAGE_SIZE * pageIndex, time, searchBy
                    ).map { it.toTroubleCode() }
                }
            }
            return LoadResult.Page(
                data = pageData,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (pageData.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return LoadResult.Error(throwable = e)
        }
    }
}