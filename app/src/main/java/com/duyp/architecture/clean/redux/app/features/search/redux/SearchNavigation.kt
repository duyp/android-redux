package com.duyp.architecture.clean.redux.app.features.search.redux

sealed class SearchNavigation {

    data class PublicRepoDetail(val id: Long) : SearchNavigation()

    data class RecentRepoDetail(val id: Long) : SearchNavigation()
}
