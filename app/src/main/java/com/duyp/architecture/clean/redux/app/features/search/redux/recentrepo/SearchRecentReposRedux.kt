package com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.recentrepo.SearchRecentRepos
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchRecentReposRedux @Inject constructor(
    private val searchRecentRepos: SearchRecentRepos,
    private val dataFormatter: DataFormatter
) {

    val searchRecentRepoSideEffect: SideEffect<SearchState, SearchAction> = { actions, _ ->
        actions.ofType(SearchRecentRepoAction.SearchRecentRepo::class.java)
            // use switch map to cancel previous search request
            .switchMap { action ->
                // clear results if search query is empty
                if (action.searchQuery.isEmpty())
                    Observable.empty()
                else
                    searchRecentRepos.get(action.searchQuery)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map<SearchRecentRepoAction> {
                            when (it) {
                                is Resource.Success -> SearchRecentRepoAction.RecentRepoSuccess(it.data)
                                is Resource.Error -> SearchRecentRepoAction.RecentRepoError(it.error)
                            }
                        }
            }
    }

    val allSideEffects = listOf(searchRecentRepoSideEffect)

    fun reducer(state: SearchState, action: SearchRecentRepoAction): SearchState {
        return when (action) {
            is SearchRecentRepoAction.RecentRepoSuccess ->
                SearchState.recentRepoLoaded(
                    state,
                    // temporary ignore viewed time util UI need to show it
                    action.items.map { it.repo },
                    dataFormatter
                )
            else -> state
        }
    }
}
