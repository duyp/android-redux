package com.duyp.architecture.clean.redux.app.features.search

import com.duyp.architecture.clean.redux.app.features.search.items.repoitem.RepoViewData

sealed class SearchItem {

    companion object {
        const val VIEW_TYPE_REPO = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_LOADING = 3
        const val VIEW_TYPE_ERROR = 4
    }

    fun getViewType(): Int = when (this) {
        is RecentRepo,
        is PublicRepo -> VIEW_TYPE_REPO
        is RecentRepoHeader,
        is PublicRepoHeader -> VIEW_TYPE_HEADER
        is Loading -> VIEW_TYPE_LOADING
        is Error -> VIEW_TYPE_ERROR
    }

    data class RecentRepo(val data: RepoViewData) : SearchItem()

    data class PublicRepo(val data: RepoViewData) : SearchItem()

    data class RecentRepoHeader(val text: String) : SearchItem()

    data class PublicRepoHeader(val text: String) : SearchItem()

    data class Error(val errorMessage: String) : SearchItem()

    object Loading : SearchItem()
}