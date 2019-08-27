package com.duyp.architecture.clean.redux.app.features.search

interface SearchAction

sealed class SearchViewAction : SearchAction {

    data class SearchRepo(val searchQuery: String) : SearchViewAction()

    object LoadNextPage : SearchViewAction()

}