package com.duyp.architecture.clean.redux.app.features.search.redux

interface SearchAction

sealed class SearchViewAction :
    SearchAction {

    data class SearchTyping(val searchQuery: String) : SearchViewAction()

    data class RecentRepoItemClick(val id: Long) : SearchViewAction()

    data class PublicRepoItemClick(val id: Long) : SearchViewAction()

    object ReloadClick : SearchViewAction()

    object ScrollToEnd : SearchViewAction()
}
