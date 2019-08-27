package com.duyp.architecture.clean.redux.app.features.search

interface SearchAction

sealed class SearchViewAction : SearchAction {

    data class SearchTyping(val searchQuery: String) : SearchViewAction()

    object LoadNextPage : SearchViewAction()

}