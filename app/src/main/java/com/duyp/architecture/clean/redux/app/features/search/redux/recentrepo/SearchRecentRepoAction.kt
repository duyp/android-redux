package com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo

import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoEntity

sealed class SearchRecentRepoAction() : SearchAction {

    data class SearchRecentRepo(val searchQuery: String) : SearchRecentRepoAction()

    data class RecentRepoSuccess(val items: List<RecentRepoEntity>) : SearchRecentRepoAction()

    data class RecentRepoError(val error: ErrorEntity) : SearchRecentRepoAction()

}
