package com.duyp.architecture.clean.redux.shareddata.api

import com.google.gson.annotations.SerializedName

/**
 * Wrap a list of data which is pageable
 */
data class PageableApiData<T>(

    @SerializedName("total_count")
    var totalCount: Long? = null,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,

    @SerializedName("first")
    var first: Int? = null,

    @SerializedName("next")
    var next: Int? = null,

    @SerializedName("prev")
    var prev: Int? = null,

    @SerializedName("last")
    var last: Int? = null,

    @SerializedName("items")
    var items: List<T>
)