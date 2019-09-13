package com.duyp.architecture.clean.redux.app.features.search.redux.publicrepo

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.common.printIfDebug
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.domain.DomainConstants
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.search.SearchPublicRepos
import com.freeletics.rxredux.SideEffect
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PublicRepoRedux @Inject constructor(
    searchPublicRepos: SearchPublicRepos,
    private val dataFormatter: DataFormatter
) {

    val searchPublicRepoFirstPageSideEffect: SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(PublicRepoAction.LoadFirstPage::class.java)
                // use switch map to cancel previous search request
                .switchMap<SearchAction> { action ->
                    val page = DomainConstants.FIRST_PAGE
                    searchPublicRepos.search(action.searchQuery, page)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map {
                            when (it) {
                                is Resource.Error -> PublicRepoAction.FirstPageError(it.error)
                                is Resource.Success -> PublicRepoAction.FirstPageSuccess(it.data)
                            }
                        }
                        .startWith(PublicRepoAction.FirstPageSearching)
                }
                // only take result util CancelSearch action fired
                .takeUntil(actions.ofType(PublicRepoAction.CancelSearch::class.java))
        }

    val searchPublicRepoNextPageSideEffect: SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(PublicRepoAction.LoadNextPage::class.java)
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
                                is Resource.Error -> PublicRepoAction.NextPageError(it.error)
                                is Resource.Success -> PublicRepoAction.NextPageSuccess(it.data)
                            }
                        }
                        .toObservable()
                        .startWith(PublicRepoAction.NextPageSearching)
                }
        }

    val allSideEffect = listOf(
        searchPublicRepoFirstPageSideEffect,
        searchPublicRepoNextPageSideEffect
    )

    fun reducer(state: SearchState, action: PublicRepoAction): SearchState {
        return when (action) {
            // first page
            is PublicRepoAction.FirstPageSearching ->
                SearchState.publicRepoFirstPageLoading(state)

            is PublicRepoAction.FirstPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }
            is PublicRepoAction.FirstPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            // next page
            is PublicRepoAction.NextPageSearching ->
                SearchState.publicRepoNextPageLoading(state)

            is PublicRepoAction.NextPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }

            is PublicRepoAction.NextPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            else -> state
        }
    }
}
