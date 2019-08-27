package com.duyp.architecture.clean.redux.app.features.search

import android.util.Log
import com.duyp.architecture.clean.redux.app.features.search.SearchInternalAction.*
import com.duyp.architecture.clean.redux.domain.DomainConstants
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.duyp.architecture.clean.redux.domain.search.SearchPublicRepoUseCase
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "SearchState"

sealed class SearchInternalAction : SearchAction {

    object FirstPageSearching : SearchInternalAction()

    data class FirstPageError(val error: ErrorEntity) : SearchInternalAction()

    data class FirstPageSuccess(val page: Int, val items: ListEntity<RepoEntity>) :
        SearchInternalAction()

    object NextPageSearching : SearchInternalAction()

    data class NextPageError(val error: ErrorEntity) : SearchInternalAction()

    data class NextPageSuccess(val page: Int, val items: ListEntity<RepoEntity>) :
        SearchInternalAction()
}

class SearchStateMachine @Inject constructor(
    private val searchPublicRepoUseCase: SearchPublicRepoUseCase
) {

    val input: Relay<SearchAction> = PublishRelay.create()

    val state: Observable<SearchState>

    init {
        state = input
            .doOnNext { Log.d(TAG, "action fired: $it") }
            .reduxStore(
                initialState = SearchState(),
                sideEffects = listOf(),
                reducer = this::reducer
            )
            .distinctUntilChanged()
            .doOnNext { Log.d(TAG, "State updated: $it") }
    }

    fun searchRepoFirstPageSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.SearchRepo::class.java)
            .debounce(300, TimeUnit.MILLISECONDS)
            .switchMap { action ->
                val page = DomainConstants.FIRST_PAGE
                searchPublicRepoUseCase.search(action.searchQuery, page)
                    .map {
                        when (it) {
                            is Resource.Error -> FirstPageError(it.error)
                            is Resource.Success -> FirstPageSuccess(page, it.data)
                        }
                    }
                    .toObservable()
                    .startWith(FirstPageSearching)
            }
    }

    fun searchRepoNextPageSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.LoadNextPage::class.java)
            .filter { !state().isNextPageLoading() }
            .switchMap { action ->
                val stateValue = state()
                val nextPage = state().currentPage + 1
                searchPublicRepoUseCase.search(stateValue.currentSearchQuery, nextPage)
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
        return state
    }
}