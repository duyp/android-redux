package com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo

import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoEntity

sealed class RecentRepoAction() : SearchAction {

    object CancelSearch : RecentRepoAction()

    data class SearchRecentRepo(val searchQuery: String) : RecentRepoAction()

    data class RecentRepoSuccess(val items: List<RecentRepoEntity>) : RecentRepoAction()

    data class RecentRepoError(val error: ErrorEntity) : RecentRepoAction()

    data class SaveRecentRepo(val repoId: Long) : RecentRepoAction()
}
