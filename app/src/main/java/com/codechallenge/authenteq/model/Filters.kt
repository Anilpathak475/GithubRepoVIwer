package com.codechallenge.authenteq.model

object Filters {
    enum class ResultSearchUsers(val value: String) {
        BY_REPOS("repositories"), BY_FOLLOWERS("followers"), BY_SCORE("score")
    }
}