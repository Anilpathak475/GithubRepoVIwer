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

    // FOR DATA ---
    private val userDataSource = UserDataSourceFactory(repository = repository, scope = ioScope)

    // OBSERVABLES ---
    val users = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState>? = switchMap(userDataSource.source) { it.getNetworkState() }

    // PUBLIC API ---

    /**
     * Fetch a list of [User] by name
     * Called each time an user hits a key through [SearchView].
     */
    fun fetchUsersByName(query: String) {
        val search = query.trim()
        if (userDataSource.getQuery() == search) return
        userDataSource.updateQuery(search, sharedPrefsManager.getFilterWhenSearchingUsers().value)
    }

    /**
     * Retry possible last paged request (ie: network issue)
     */
    fun refreshFailedRequest() =
        userDataSource.getSource()?.retryFailedQuery()

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() =
        userDataSource.getSource()?.refresh()

    /**
     * Returns filter [Filters.ResultSearchUsers] used to sort "search" request
     */
    fun getFilterWhenSearchingUsers() =
        sharedPrefsManager.getFilterWhenSearchingUsers()

    /**
     * Saves filter [Filters.ResultSearchUsers] used to sort "search" request
     */
    fun saveFilterWhenSearchingUsers(filter: Filters.ResultSearchUsers) =
        sharedPrefsManager.saveFilterWhenSearchingUsers(filter)

    /**
     * Returns current search query
     */
    fun getCurrentQuery() =
        userDataSource.getQuery()

    // UTILS ---

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .setPageSize(5 * 2)
        .build()
}