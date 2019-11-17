package com.huy.kotlin.ui.paging


import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.huy.kotlin.base.arch.BaseViewModel

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/11
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingVM : BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val mList = mutableListOf<PagingItem>()

    private var listSize: Int = 0

    override fun onStart() {
        for (i in 1..100) mList.add(PagingItem(i))
        listSize = mList.size
    }

    fun liveData(initPage: Int): LiveData<PagedList<PagingItem>?> {

        var config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setEnablePlaceholders(true)
                .build()

        val dataSource = DataSourceFactory()

        return LivePagedListBuilder(dataSource, config)
                .setInitialLoadKey(initPage)
                .build()
    }

    inner class DataSourceFactory : DataSource.Factory<Int, PagingItem>() {
        override fun create(): DataSource<Int, PagingItem> {
            return PagingDataSource()
        }
    }

    inner class PagingDataSource : PageKeyedDataSource<Int, PagingItem>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PagingItem>) {
            val list = mutableListOf<PagingItem>()
            for (i in 0..9) list.add(mList[i])
            callback.onResult(list, null, 2)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PagingItem>) {
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PagingItem>) {
            val startIndex = (params.key - 1) * PAGE_SIZE
            val endIndex = startIndex + PAGE_SIZE - 1
            val list = mutableListOf<PagingItem>()
            for (i in startIndex..endIndex) if (i < listSize) list.add(mList[i])
            callback.onResult(list, params.key + 1)

        }

    }

}