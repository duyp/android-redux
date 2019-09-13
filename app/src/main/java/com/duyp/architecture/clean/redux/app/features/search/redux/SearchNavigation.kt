package com.duyp.architecture.clean.redux.app.features.search.redux

sealed class SearchNavigation {

    data class RepoDetail(val id: Long) : SearchNavigation()
}