package com.duyp.architecture.clean.redux.app.features.search.redux

import android.util.Log
import com.duyp.architecture.clean.redux.app.common.getMessage
import com.duyp.architecture.clean.redux.app.common.printIfDebug
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchInternalAction.*
import com.duyp.architecture.clean.redux.domain.DomainConstants
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.repo.GetRecentRepoUseCase
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.duyp.architecture.clean.redux.domain.search.SearchPublicRepoUseCase
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "SearchState"

sealed class SearchInternalAction : SearchAction {

    // recent repo
    data class RecentRepoSuccess(val items: List<RepoEntity>) : SearchInternalAction()

    // public repo first page
    object FirstPageSearching : SearchInternalAction()

    data class FirstPageError(val error: ErrorEntity) : SearchInternalAction()

    data class FirstPageSuccess(val page: Int, val items: ListEntity<RepoEntity>) :
        SearchInternalAction()

    // public repo next page
    object NextPageSearching : SearchInternalAction()

    data class NextPageError(val error: ErrorEntity) : SearchInternalAction()

    data class NextPageSuccess(val page: Int, val items: ListEntity<RepoEntity>) :
        SearchInternalAction()

    override fun toString(): String = this::class.java.simpleName
}

class SearchStateMachine @Inject constructor(
    private val searchPublicRepoUseCase: SearchPublicRepoUseCase,
    private val getRecentRepoUseCase: GetRecentRepoUseCase
) {

    val input: Relay<SearchAction> = PublishRelay.create()

    val state: Observable<SearchState>

    init {
        state = input
            .doOnNext { Log.d(TAG, "action fired: $it") }
            .reduxStore(
                initialState = SearchState(),
                sideEffects = listOf(
                    searchRecentRepoFirstPageSideEffect(),
                    searchPublicRepoFirstPageSideEffect(),
                    searchPublicRepoNextPageSideEffect()
                ),
                reducer = this::reducer
            )
            .distinctUntilChanged()
            .doOnNext { Log.d(TAG, "State updated: ${it.items.size} items") }
    }

    fun searchRecentRepoFirstPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                .switchMap { action ->
                    if (action.searchQuery.isEmpty())
                        Observable.just(RecentRepoSuccess(emptyList()))
                    else
                        getRecentRepoUseCase.get(action.searchQuery)
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .onErrorReturnItem(emptyList())
                            .map<SearchAction> {
                                RecentRepoSuccess(it)
                            }
                }
        }

    // todo cancel previous search when search query is empty
    fun searchPublicRepoFirstPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter {
                    it.searchQuery.isNotEmpty()
                }
                .switchMap { action ->
                    val page = DomainConstants.FIRST_PAGE
                    searchPublicRepoUseCase.search(action.searchQuery, page)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map {
                            when (it) {
                                is Resource.Error -> FirstPageError(it.error)
                                is Resource.Success -> FirstPageSuccess(page, it.data)
                            }
                        }
                        .startWith(FirstPageSearching)
                }
        }

    fun searchPublicRepoNextPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(SearchViewAction.LoadNextPage::class.java)
                .filter { !state().isLoading() }
                .switchMap {
                    val stateValue = state()
                    val nextPage = state().currentPage + 1
                    searchPublicRepoUseCase.search(stateValue.currentSearchQuery, nextPage)
                        .subscribeOn(Schedulers.io())
                        .map {
                            when (it) {
                                is Resource.Error -> NextPageError(it.error)
                                is Resource.Success -> NextPageSuccess(nextPage, it.data)
                            }
                        }
                        .toObservable()
                        .startWith(NextPageSearching)
                }
        }

    fun reducer(state: SearchState, action: SearchAction): SearchState {
        Log.d(TAG, "reducer reacts on action $action")
        return when (action) {
            is SearchViewAction.SearchTyping -> {
                SearchState.currentSearchQueryUpdated(
                    state,
                    action.searchQuery
                )
            }
            // recent repos
            is RecentRepoSuccess -> {
                SearchState.recentRepoLoaded(
                    state,
                    action.items
                )
            }
            // first page
            is FirstPageSearching -> {
                SearchState.publicRepoFirstPageLoading(
                    state
                )
            }
            is FirstPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(
                    state,
                    action.error.getMessage()
                )
            }
            is FirstPageSuccess -> {
                SearchState.publicRepoLoaded(
                    state,
                    action.page,
                    action.items
                )
            }
            // next page
            is NextPageSearching -> {
                SearchState.publicRepoNextPageLoading(
                    state
                )
            }
            is NextPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(
                    state,
                    action.error.getMessage()
                )
            }
            is NextPageSuccess -> {
                SearchState.publicRepoLoaded(
                    state,
                    action.page,
                    action.items
                )
            }
            else -> state
        }
    }
}
