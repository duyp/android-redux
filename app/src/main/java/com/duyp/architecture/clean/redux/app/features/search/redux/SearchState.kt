package com.duyp.architecture.clean.redux.app.features.search.redux

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.features.search.SearchItem
import com.duyp.architecture.clean.redux.app.features.search.items.repoitem.toViewData
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

data class SearchState(
    val currentSearchQuery: String = "",
    val nextPage: Int? = null,
    private val canLoadMore: Boolean = false,
    val items: List<SearchItem> = emptyList()
) {

    fun isLoading() = items.contains(SearchItem.Loading)

    fun canLoadMore() = canLoadMore && !isLoading()

    fun isNextPageError() = nextPage != null && items.count { it is SearchItem.Error } > 0

    companion object {

        fun currentSearchQueryUpdated(currentState: SearchState, query: String) = currentState.copy(
            currentSearchQuery = query
        )

        fun clearResults(currentState: SearchState) = currentState.copy(
            currentSearchQuery = "",
            items = emptyList()
        )

        fun recentRepoLoaded(
            currentState: SearchState,
            list: List<RepoEntity>,
            dataFormatter: DataFormatter
        ) = currentState.copy(
            // recent repo is always come before public repo so we can safely remove all public repo items
            // public repo requests api search and uses debounce when typing
            items =
            if (list.isEmpty())
                emptyList()
            else
                listOf(SearchItem.RecentRepoHeader("Recent viewed repositories:")) +
                        list.map {
                            SearchItem.RecentRepo(data = it.toViewData(dataFormatter))
                        }
        )

        fun publicRepoFirstPageLoading(currentState: SearchState) = currentState.copy(
            items = currentState.items
                // clear all public repo items
                .filterNot {
                    it is SearchItem.PublicRepoHeader ||
                            it is SearchItem.Loading ||
                            it is SearchItem.Error ||
                            it is SearchItem.PublicRepo
                }
                    // append header and loading
                    + SearchItem.PublicRepoHeader("Searching public repositories...")
                    + SearchItem.Loading
        )

        fun publicRepoNextPageLoading(currentState: SearchState) = currentState.copy(
            items = currentState.items
                // remove current loading and error
                .filterNot {
                    it is SearchItem.Loading || it is SearchItem.Error
                }
                    // append loading at the end
                    + SearchItem.Loading
        )

        fun publicRepoError(currentState: SearchState, errorMessage: String) =
            currentState.copy(
                items = currentState.items
                    // first remove loading or already presented error item
                    .filterNot {
                        it is SearchItem.Loading || it is SearchItem.Error
                    }
                        + SearchItem.Error(errorMessage)
            )

        fun publicRepoLoaded(
            currentState: SearchState,
            list: ListEntity<RepoEntity>,
            dataFormatter: DataFormatter
        ) =
            currentState.copy(
                canLoadMore = list.hasMore(),
                nextPage = list.nextPage(),
                items = currentState.items
                    // first update the header
                    .map {
                        if (it is SearchItem.PublicRepoHeader)
                            it.copy(text = "Found ${list.totalCount()} public repositories:")
                        else
                            it
                    }
                    // remove the loading or error item
                    .filterNot {
                        it is SearchItem.Loading || it is SearchItem.Error
                    }
                        // append results
                        + list.items().map { SearchItem.PublicRepo(it.toViewData(dataFormatter)) }
            )
    }
}
