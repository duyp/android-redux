package com.duyp.architecture.clean.redux.app.features.search

import com.duyp.architecture.clean.redux.app.features.search.items.repoitem.toViewData
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

data class SearchState(
    val currentSearchQuery: String = "",
    val currentPage: Int = 0,
    val canLoadMore: Boolean = false,
    val items: List<SearchItem> = emptyList()
) {

    fun isLoading() = items.contains(SearchItem.Loading)

    companion object {

        fun currentSearchQueryUpdated(currentState: SearchState, query: String) = currentState.copy(
            currentSearchQuery = query
        )

        fun recentRepoLoaded(currentState: SearchState, list: List<RepoEntity>) = currentState.copy(
            // recent repo is always come before public repo show we can safely remove all public repo items
            items = listOf(SearchItem.RecentRepoHeader("Recent repositories:")) +
                    list.map {
                        SearchItem.RecentRepo(data = it.toViewData())
                    }
        )

        fun publicRepoFirstPageLoading(currentState: SearchState) = currentState.copy(
            items = currentState.items
                // clear all public repo items
                .filterNot {
                    it is SearchItem.PublicRepoHeader ||
                            it is SearchItem.Loading ||
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

        fun publicRepoLoaded(currentState: SearchState, page: Int, list: ListEntity<RepoEntity>) =
            currentState.copy(
                canLoadMore = list.hasMore(),
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
                        + list.items().map { SearchItem.PublicRepo(it.toViewData()) }
            )
    }
}