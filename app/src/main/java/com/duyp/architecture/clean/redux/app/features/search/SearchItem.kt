package com.duyp.architecture.clean.redux.app.features.search

import com.duyp.architecture.clean.redux.app.features.search.items.repoitem.RepoViewData

sealed class SearchItem {

    companion object {
        const val VIEW_TYPE_REPO = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_LOADING = 3
    }

    fun getViewType(): Int = when (this) {
        is Repo -> VIEW_TYPE_REPO
        is RecentRepoHeader,
        is PublicRepoHeader -> VIEW_TYPE_HEADER
        is NextPageLoading -> VIEW_TYPE_LOADING
    }

    data class Repo(val data: RepoViewData) : SearchItem()

    data class RecentRepoHeader(val text: String) : SearchItem()

    data class PublicRepoHeader(val text: String) : SearchItem()

    object NextPageLoading : SearchItem()
}