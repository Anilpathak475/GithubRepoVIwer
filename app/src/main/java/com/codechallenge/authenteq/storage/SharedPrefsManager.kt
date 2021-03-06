package com.codechallenge.authenteq.storage

import android.content.Context
import android.content.SharedPreferences
import com.codechallenge.authenteq.extensions.setValue
import com.codechallenge.authenteq.model.Filters

class SharedPrefsManager(private val context: Context) {

    private fun get(): SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)


    fun getFilterWhenSearchingUsers(): Filters.ResultSearchUsers {
        return when (get().getString(KEY_FILTERS, Filters.ResultSearchUsers.BY_SCORE.value)) {
            Filters.ResultSearchUsers.BY_SCORE.value -> Filters.ResultSearchUsers.BY_SCORE
            Filters.ResultSearchUsers.BY_REPOS.value -> Filters.ResultSearchUsers.BY_REPOS
            Filters.ResultSearchUsers.BY_FOLLOWERS.value -> Filters.ResultSearchUsers.BY_FOLLOWERS
            else -> throw IllegalStateException("Filter not recognized")
        }
    }


    fun saveFilterWhenSearchingUsers(filters: Filters.ResultSearchUsers) {
        get().setValue(KEY_FILTERS, filters.value)
    }

    companion object {
        private const val SP_NAME = "GithubAppPrefs"
        private const val KEY_FILTERS = "KEY_FILTERS"
    }
}