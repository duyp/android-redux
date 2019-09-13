package com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.common.printIfDebug
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.domain.DomainConstants
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.search.SearchPublicRepos
import com.freeletics.rxredux.SideEffect
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val MIN_SEARCH_QUERY_LENGTH = 3

class SearchPublicRepoRedux @Inject constructor(
    searchPublicRepos: SearchPublicRepos,
    private val dataFormatter: DataFormatter
) {

    val searchPublicRepoFirstPageSideEffect: SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchPublicRepoAction.LoadFirstPage::class.java)
                // use switch map to cancel previous search request
                .switchMap<SearchAction> { action ->
                    if (action.searchQuery.length < MIN_SEARCH_QUERY_LENGTH) {
                        return@switchMap Observable.empty()
                    }
                    val page = DomainConstants.FIRST_PAGE
                    searchPublicRepos.search(action.searchQuery, page)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map {
                            when (it) {
                                is Resource.Error -> SearchPublicRepoAction.FirstPageError(it.error)
                                is Resource.Success -> SearchPublicRepoAction.FirstPageSuccess(it.data)
                            }
                        }
                        .startWith(SearchPublicRepoAction.FirstPageSearching)
                }
        }

    val searchPublicRepoNextPageSideEffect: SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(SearchPublicRepoAction.LoadNextPage::class.java)
                .filter {
                    val stateValue = state()
                    // only load next page if has next page and not is loading
                    stateValue.nextPage != null && !stateValue.isLoading()
                }
                .switchMap {
                    val stateValue = state()
                    val nextPage = stateValue.nextPage!!
                    searchPublicRepos.search(stateValue.currentSearchQuery, nextPage)
                        .subscribeOn(Schedulers.io())
                        .map {
                            when (it) {
                                is Resource.Error -> SearchPublicRepoAction.NextPageError(it.error)
                                is Resource.Success -> SearchPublicRepoAction.NextPageSuccess(it.data)
                            }
                        }
                        .toObservable()
                        .startWith(SearchPublicRepoAction.NextPageSearching)
                }
        }

    val allSideEffect = listOf(
        searchPublicRepoFirstPageSideEffect,
        searchPublicRepoNextPageSideEffect
    )

    fun reducer(state: SearchState, action: SearchPublicRepoAction): SearchState {
        return when (action) {
            // first page
            is SearchPublicRepoAction.FirstPageSearching ->
                SearchState.publicRepoFirstPageLoading(state)

            is SearchPublicRepoAction.FirstPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }
            is SearchPublicRepoAction.FirstPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            // next page
            is SearchPublicRepoAction.NextPageSearching ->
                SearchState.publicRepoNextPageLoading(state)

            is SearchPublicRepoAction.NextPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }

            is SearchPublicRepoAction.NextPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            else -> state
        }
    }
}
