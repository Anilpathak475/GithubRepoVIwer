package com.codechallenge.authenteq.model

import com.codechallenge.authenteq.api.UserService

object Filters {

    /**
     * Filters used by [UserService]
     * to sort "search" queries
     */
    enum class ResultSearchUsers(val value: String) {
        BY_REPOS("repositories"), BY_FOLLOWERS("followers"), BY_SCORE("score")
    }
}