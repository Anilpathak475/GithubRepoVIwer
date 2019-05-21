package com.codechallenge.authenteq.ui.user.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.codechallenge.authenteq.api.NetworkState
import com.codechallenge.authenteq.base.BaseViewModel
import com.codechallenge.authenteq.model.Filters
import com.codechallenge.authenteq.model.User
import com.codechallenge.authenteq.pagination.datasource.UserDataSourceFactory
import com.codechallenge.authenteq.repository.UserRepository
import com.codechallenge.authenteq.storage.SharedPrefsManager

class SearchUserViewModel(
    repository: UserRepository,
    private val sharedPrefsManager: SharedPrefsManager
) : BaseViewModel() {

    private val userDataSource = UserDataSourceFactory(repository = repository, scope = ioScope)

    val users = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState>? = switchMap(userDataSource.source) { it.getNetworkState() }


    fun fetchUsersByName(query: String) {
        val search = query.trim()
        if (userDataSource.getQuery() == search) return
        userDataSource.updateQuery(search, sharedPrefsManager.getFilterWhenSearchingUsers().value)
    }

    fun refreshFailedRequest() =
        userDataSource.getSource()?.retryFailedQuery()


    fun refreshAllList() =
        userDataSource.getSource()?.refresh()


    fun getCurrentQuery() =
        userDataSource.getQuery()

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .setPageSize(5 * 2)
        .build()
}