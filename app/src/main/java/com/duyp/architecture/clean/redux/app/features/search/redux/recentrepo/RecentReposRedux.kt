package com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.recentrepo.AddRecentRepo
import com.duyp.architecture.clean.redux.domain.recentrepo.SearchRecentRepos
import com.freeletics.rxredux.SideEffect
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class RecentReposRedux @Inject constructor(
    private val searchRecentRepos: SearchRecentRepos,
    private val addRecentRepo: AddRecentRepo,
    private val dataFormatter: DataFormatter
) {

    val searchRecentRepoSideEffect: SideEffect<SearchState, SearchAction> = { actions, _ ->
        actions.ofType(RecentRepoAction.SearchRecentRepo::class.java)
            // use switch map to cancel previous search request
            .switchMap { action ->
                searchRecentRepos.get(action.searchQuery)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<RecentRepoAction> {
                        when (it) {
                            is Resource.Success -> RecentRepoAction.RecentRepoSuccess(it.data)
                            is Resource.Error -> RecentRepoAction.RecentRepoError(it.error)
                        }
                    }
            }
            // only take result util CancelSearch action fired
            .takeUntil(actions.ofType(RecentRepoAction.CancelSearch::class.java))
    }

    val saveRecentRepoSideEffect: SideEffect<SearchState, SearchAction> = { actions, _ ->
        actions.ofType(RecentRepoAction.SaveRecentRepo::class.java)
            .switchMap<SearchAction> {
                addRecentRepo.add(it.repoId, Date())
                    .subscribeOn(Schedulers.io())
                    .onErrorComplete()
                    .toObservable()
            }
    }

    val allSideEffects = listOf(searchRecentRepoSideEffect, saveRecentRepoSideEffect)

    fun reducer(state: SearchState, action: RecentRepoAction): SearchState {
        return when (action) {
            is RecentRepoAction.RecentRepoSuccess ->
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
