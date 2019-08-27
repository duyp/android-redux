package com.duyp.architecture.clean.redux.app.features.search

data class SearchState(
    val isShowProgress: Boolean = false,
    val currentSearchQuery: String = "",
    val currentPage: Int = 0,
    val hasMore: Boolean = false,
    val items: List<SearchItem> = emptyList()
) {

    fun isNextPageLoading() = items.contains(SearchItem.NextPageLoading)
}