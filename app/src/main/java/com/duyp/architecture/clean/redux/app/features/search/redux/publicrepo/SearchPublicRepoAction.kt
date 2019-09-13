package com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo

import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

sealed class SearchPublicRepoAction : SearchAction {

    data class LoadFirstPage(val searchQuery: String) : SearchPublicRepoAction()

    object FirstPageSearching : SearchPublicRepoAction()

    data class FirstPageError(val error: ErrorEntity) : SearchPublicRepoAction()

    data class FirstPageSuccess(val items: ListEntity<RepoEntity>) :
        SearchPublicRepoAction()

    // public repo next page
    data class LoadNextPage(val searchQuery: String) : SearchPublicRepoAction()

    object NextPageSearching : SearchPublicRepoAction()

    data class NextPageError(val error: ErrorEntity) : SearchPublicRepoAction()

    data class NextPageSuccess(val items: ListEntity<RepoEntity>) :
        SearchPublicRepoAction()

}
