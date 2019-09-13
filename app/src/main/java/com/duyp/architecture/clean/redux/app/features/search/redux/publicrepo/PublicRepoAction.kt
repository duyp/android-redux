package com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo

import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

sealed class PublicRepoAction : SearchAction {

    object CancelSearch : PublicRepoAction()

    data class LoadFirstPage(val searchQuery: String) : PublicRepoAction()

    object FirstPageSearching : PublicRepoAction()

    data class FirstPageError(val error: ErrorEntity) : PublicRepoAction()

    data class FirstPageSuccess(val items: ListEntity<RepoEntity>) :
        PublicRepoAction()

    // public repo next page
    data class LoadNextPage(val searchQuery: String) : PublicRepoAction()

    object NextPageSearching : PublicRepoAction()

    data class NextPageError(val error: ErrorEntity) : PublicRepoAction()

    data class NextPageSuccess(val items: ListEntity<RepoEntity>) :
        PublicRepoAction()

}
