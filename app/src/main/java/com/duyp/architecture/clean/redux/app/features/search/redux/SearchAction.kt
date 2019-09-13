package com.duyp.architecture.clean.redux.app.features.search.redux

interface SearchAction

sealed class SearchViewAction :
    SearchAction {

    data class SearchTyping(val searchQuery: String) : SearchViewAction()

    data class RepoItemClick(val id: Long) : SearchViewAction()

    object ReloadClick : SearchViewAction()

    object LoadNextPage : SearchViewAction()
}
