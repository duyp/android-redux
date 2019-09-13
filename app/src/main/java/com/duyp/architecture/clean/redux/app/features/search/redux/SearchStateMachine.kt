package com.duyp.architecture.clean.redux.app.features.search.redux

import android.util.Log
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchInternalAction.ClearResults
import com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo.PublicRepoAction
import com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo.PublicRepoRedux
import com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo.RecentRepoAction
import com.duyp.architecture.clean.redux.app.features.search.redux.recentrepo.RecentReposRedux
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "SearchState"

private const val MIN_SEARCH_QUERY_LENGTH = 3

private sealed class SearchInternalAction : SearchAction {

    object ClearResults : SearchInternalAction()

    override fun toString(): String = this::class.java.simpleName
}

class SearchStateMachine @Inject constructor(
    private val recentReposRedux: RecentReposRedux,
    private val searchPublicRepoRedux: PublicRepoRedux
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
                    recentRepoClickSideEffect(),
                    publicRepoClickSideEffect()
                ) + recentReposRedux.allSideEffects + searchPublicRepoRedux.allSideEffect,
                reducer = this::reducer
            )
            .distinctUntilChanged()
            .doOnNext { Log.d(TAG, "State updated: ${it.items.size} items") }
    }

    /**
     * typing: search recent repo after every typing, if search query is empty -> clear results
     * and cancel previous recent repo search
     */
    fun recentRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.SearchTyping::class.java)
            .switchMap {
                if (it.searchQuery.isEmpty())
                    Observable.fromArray(ClearResults, RecentRepoAction.CancelSearch)
                else
                    Observable.just(RecentRepoAction.SearchRecentRepo(it.searchQuery))
            }
    }

    /**
     * typing: the query length must be >= [MIN_SEARCH_QUERY_LENGTH] otherwise previous search
     * request will be canceled
     */
    fun publicRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                // debounce to avoid too much requests
                .debounce(500, TimeUnit.MILLISECONDS)
                .map {
                    if (it.searchQuery.length < MIN_SEARCH_QUERY_LENGTH)
                        PublicRepoAction.CancelSearch
                    else
                        PublicRepoAction.LoadFirstPage(it.searchQuery)
                }
        }

    /**
     * scroll to end of list: load next page
     */
    fun scrollToLoadNextPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(SearchViewAction.ScrollToEnd::class.java)
                .filter {
                    // as the error view has its own reload button, so lets user do it manually
                    !state().isNextPageError()
                }
                .map {
                    PublicRepoAction.LoadNextPage(searchQuery = state().currentSearchQuery)
                }
        }

    /**
     * Reload click: load first page or load next page
     */
    fun reloadSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.ReloadClick::class.java)
            .map {
                val stateValue = state()
                if (stateValue.nextPage == null)
                    PublicRepoAction.LoadFirstPage(searchQuery = stateValue.currentSearchQuery)
                else
                    PublicRepoAction.LoadNextPage(searchQuery = stateValue.currentSearchQuery)
            }
    }


    /**
     * Recent repo click: open repo detail as well as saving recent viewed repo
     */
    fun recentRepoClickSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.RecentRepoItemClick::class.java)
            .doOnNext {
                navigation.accept(SearchNavigation.RecentRepoDetail(it.id))
            }
            // just navigate then do nothing
            .filter { false }
    }

    /**
     * Public repo click: open repo detail as well as saving recent viewed repo
     */
    fun publicRepoClickSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.PublicRepoItemClick::class.java)
            .doOnNext {
                navigation.accept(SearchNavigation.PublicRepoDetail(it.id))
            }
            .map {
                RecentRepoAction.SaveRecentRepo(repoId = it.id)
            }
    }

    fun reducer(state: SearchState, action: SearchAction): SearchState {
        Log.d(TAG, "reducer reacts on action $action")
        return when (action) {

            is SearchViewAction.SearchTyping ->
                SearchState.currentSearchQueryUpdated(state, action.searchQuery)

            is ClearResults -> SearchState.clearResults(state)

            is RecentRepoAction -> recentReposRedux.reducer(state, action)

            is PublicRepoAction -> searchPublicRepoRedux.reducer(state, action)

            else -> state
        }
    }
}
