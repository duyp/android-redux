package com.duyp.architecture.clean.redux.app.features.search.redux

import android.util.Log
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchInternalAction.ClearResults
import com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo.SearchPublicRepoAction
import com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo.SearchPublicRepoRedux
import com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo.SearchRecentRepoAction
import com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo.SearchRecentReposRedux
import com.duyp.architecture.clean.redux.domain.recentrepo.AddRecentRepo
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "SearchState"

private sealed class SearchInternalAction : SearchAction {

    object ClearResults : SearchInternalAction()

    override fun toString(): String = this::class.java.simpleName
}

class SearchStateMachine @Inject constructor(
    private val searchRecentReposRedux: SearchRecentReposRedux,
    private val searchPublicRepoRedux: SearchPublicRepoRedux,
    private val addRecentRepo: AddRecentRepo
) {

    val input: Relay<SearchAction> = PublishRelay.create()

    val navigation: Relay<SearchNavigation> = PublishRelay.create()

    val state: Observable<SearchState>

    init {
        state = input
            .doOnNext { Log.d(TAG, "action fired: $it") }
            .reduxStore(
                initialState = SearchState(),
                sideEffects = listOf(
                    recentRepoTypingSideEffect(),
                    publicRepoTypingSideEffect(),
                    scrollToLoadNextPageSideEffect(),
                    reloadSideEffect(),
                    saveRecentRepoSideEffect(),
                    navigationSideEffect()
                ) + searchRecentReposRedux.allSideEffects + searchPublicRepoRedux.allSideEffect,
                reducer = this::reducer
            )
            .distinctUntilChanged()
            .doOnNext { Log.d(TAG, "State updated: ${it.items.size} items") }
    }

    fun recentRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.SearchTyping::class.java)
            .switchMap {
                if (it.searchQuery.isEmpty())
                    Observable.just(ClearResults)
                else
                    Observable.just(SearchRecentRepoAction.SearchRecentRepo(it.searchQuery))
            }
    }

    fun publicRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                // debounce to avoid too much requests
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { SearchPublicRepoAction.LoadFirstPage(it.searchQuery) }
        }

    fun scrollToLoadNextPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(SearchViewAction.LoadNextPage::class.java)
                .filter {
                    // as the error view has its own reload button, so lets user do it manually
                    !state().isNextPageError()
                }
                .map {
                    SearchPublicRepoAction.LoadNextPage(searchQuery = state().currentSearchQuery)
                }
        }


    fun reloadSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.ReloadClick::class.java)
            .map {
                val stateValue = state()
                if (stateValue.nextPage == null)
                    SearchPublicRepoAction.LoadFirstPage(searchQuery = stateValue.currentSearchQuery)
                else
                    SearchPublicRepoAction.LoadNextPage(searchQuery = stateValue.currentSearchQuery)
            }
    }

    fun saveRecentRepoSideEffect(): SideEffect<SearchState, SearchAction> = { actions, _ ->
        actions.ofType(SearchViewAction.PublicRepoItemClick::class.java)
            .switchMap<SearchAction> {
                addRecentRepo.add(it.id, Date())
                    .subscribeOn(Schedulers.io())
                    .onErrorComplete()
                    .toObservable()
            }
    }

    fun navigationSideEffect(): SideEffect<SearchState, SearchAction> = { actions, _ ->
        actions
            .doOnNext {
                when (it) {
                    is SearchViewAction.RecentRepoItemClick ->
                        navigation.accept(SearchNavigation.RecentRepoDetail(it.id))
                    is SearchViewAction.PublicRepoItemClick ->
                        navigation.accept(SearchNavigation.PublicRepoDetail(it.id))
                }
            }
            // just navigate without changing state
            .filter { false }
    }

    fun reducer(state: SearchState, action: SearchAction): SearchState {
        Log.d(TAG, "reducer reacts on action $action")
        return when (action) {

            is SearchViewAction.SearchTyping ->
                SearchState.currentSearchQueryUpdated(state, action.searchQuery)

            is ClearResults -> SearchState.clearResults(state)

            is SearchRecentRepoAction -> searchRecentReposRedux.reducer(state, action)

            is SearchPublicRepoAction -> searchPublicRepoRedux.reducer(state, action)

            else -> state
        }
    }
}
