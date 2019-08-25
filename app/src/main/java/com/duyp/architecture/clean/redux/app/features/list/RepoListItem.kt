package com.duyp.architecture.clean.redux.app.features.list

import com.duyp.architecture.clean.redux.app.features.list.item.RepoViewData

sealed class RepoListItem {

    companion object {
        const val VIEW_TYPE_REPO = 1
        const val VIEW_TYPE_LOADING = 2
    }

    data class Repo(val data: RepoViewData) : RepoListItem()

    object Loading : RepoListItem()

    fun getViewType(): Int = when (this) {
        is Repo -> VIEW_TYPE_REPO
        is Loading -> VIEW_TYPE_LOADING
    }

}