package com.duyp.architecture.clean.redux.app.features.search.redux

import android.util.Log
import com.duyp.architecture.clean.redux.app.common.DataFormatter
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

private const val MIN_SEARCH_QUERY_LENGTH = 3

private sealed class SearchInternalAction : SearchAction {

    object ClearResults : SearchInternalAction()

    // recent repo
    data class RecentRepoSuccess(val items: List<RepoEntity>) : SearchInternalAction()

    // public repo first page
    data class LoadFirstPage(val searchQuery: String) : SearchInternalAction()

    object FirstPageSearching : SearchInternalAction()

    data class FirstPageError(val error: ErrorEntity) : SearchInternalAction()

    data class FirstPageSuccess(val items: ListEntity<RepoEntity>) :
        SearchInternalAction()

    // public repo next page
    data class LoadNextPage(val searchQuery: String) : SearchInternalAction()

    object NextPageSearching : SearchInternalAction()

    data class NextPageError(val error: ErrorEntity) : SearchInternalAction()

    data class NextPageSuccess(val items: ListEntity<RepoEntity>) :
        SearchInternalAction()

    override fun toString(): String = this::class.java.simpleName
}

class SearchStateMachine @Inject constructor(
    private val searchPublicRepoUseCase: SearchPublicRepoUseCase,
    private val getRecentRepoUseCase: GetRecentRepoUseCase,
    private val dataFormatter: DataFormatter
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
                    searchRecentRepoTypingSideEffect(),
                    searchPublicRepoTypingSideEffect(),
                    scrollToLoadNextPageSideEffect(),
                    reloadSideEffect(),
                    searchPublicRepoFirstPageSideEffect(),
                    searchPublicRepoNextPageSideEffect(),
                    navigationSideEffect()
                ),
                reducer = this::reducer
            )
            .distinctUntilChanged()
            .doOnNext { Log.d(TAG, "State updated: ${it.items.size} items") }
    }

    fun searchRecentRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                // use switch map to cancel previous search request
                .switchMap { action ->
                    // clear results if search query is empty
                    if (action.searchQuery.isEmpty())
                        Observable.just(ClearResults)
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

    fun searchPublicRepoTypingSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(SearchViewAction.SearchTyping::class.java)
                // debounce to avoid too much requests
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { LoadFirstPage(it.searchQuery) }
        }

    fun searchPublicRepoFirstPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, _ ->
            actions.ofType(LoadFirstPage::class.java)
                // use switch map to cancel previous search request
                .switchMap<SearchAction> { action ->
                    if (action.searchQuery.length < MIN_SEARCH_QUERY_LENGTH) {
                        return@switchMap Observable.empty()
                    }
                    val page = DomainConstants.FIRST_PAGE
                    searchPublicRepoUseCase.search(action.searchQuery, page)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map {
                            when (it) {
                                is Resource.Error -> FirstPageError(it.error)
                                is Resource.Success -> FirstPageSuccess(it.data)
                            }
                        }
                        .startWith(FirstPageSearching)
                }
        }

    fun scrollToLoadNextPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(SearchViewAction.LoadNextPage::class.java)
                .filter {
                    // as the error view has its own reload button, so lets user do it manually
                    !state().isNextPageError()
                }
                .map {
                    LoadNextPage(searchQuery = state().currentSearchQuery)
                }
        }

    fun searchPublicRepoNextPageSideEffect(): SideEffect<SearchState, SearchAction> =
        { actions, state ->
            actions.ofType(LoadNextPage::class.java)
                .filter {
                    val stateValue = state()
                    // only load next page if has next page and not is loading
                    stateValue.nextPage != null && !stateValue.isLoading()
                }
                .switchMap {
                    val stateValue = state()
                    val nextPage = stateValue.nextPage!!
                    searchPublicRepoUseCase.search(stateValue.currentSearchQuery, nextPage)
                        .subscribeOn(Schedulers.io())
                        .map {
                            when (it) {
                                is Resource.Error -> NextPageError(it.error)
                                is Resource.Success -> NextPageSuccess(it.data)
                            }
                        }
                        .toObservable()
                        .startWith(NextPageSearching)
                }
        }

    fun reloadSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions.ofType(SearchViewAction.ReloadClick::class.java)
            .map {
                val stateValue = state()
                if (stateValue.nextPage == null)
                    LoadFirstPage(searchQuery = stateValue.currentSearchQuery)
                else
                    LoadNextPage(searchQuery = stateValue.currentSearchQuery)
            }
    }

    fun navigationSideEffect(): SideEffect<SearchState, SearchAction> = { actions, state ->
        actions
            .doOnNext {
                when (it) {
                    is SearchViewAction.RepoItemClick ->
                        navigation.accept(SearchNavigation.RepoDetail(it.id))
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

            is ClearResults ->
                SearchState.clearResults(state)

            // recent repos
            is RecentRepoSuccess ->
                SearchState.recentRepoLoaded(state, action.items, dataFormatter)

            // first page
            is FirstPageSearching ->
                SearchState.publicRepoFirstPageLoading(state)

            is FirstPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }
            is FirstPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            // next page
            is NextPageSearching ->
                SearchState.publicRepoNextPageLoading(state)

            is NextPageError -> {
                action.error.originalThrowable.printIfDebug()
                SearchState.publicRepoError(state, action.error.message)
            }

            is NextPageSuccess ->
                SearchState.publicRepoLoaded(state, action.items, dataFormatter)

            else -> state
        }
    }
}
