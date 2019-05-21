package com.codechallenge.authenteq.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<User>
)