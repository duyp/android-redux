package com.duyp.architecture.clean.redux.app.features.list

sealed class RepoListState(currentState: RepoListState?) {

    open val items: List<RepoListItem> = currentState?.items ?: emptyList()

    object Init : RepoListState(null)

    class DataLoaded(
        currentState: RepoListState,
        override val items: List<RepoListItem>
    ) : RepoListState(currentState)

    class DataLoadError(
        currentState: RepoListState,
        val errorMessage: String
    ) : RepoListState(currentState)
}