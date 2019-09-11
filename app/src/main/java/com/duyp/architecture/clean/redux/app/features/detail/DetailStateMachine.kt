package com.duyp.architecture.clean.redux.app.features.detail

import android.util.Log
import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.repo.GetRepoUseCase
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "DetailState"

data class DetailState(
    val viewData: DetailViewData? = null,
    val errorMessage: String? = null
)

sealed class DetailAction {

    data class LoadRepoDetail(val id: Long) : DetailAction()
}

private data class DetailLoadedAction(val entity: RepoEntity) : DetailAction()

private data class DetailLoadErrorAction(val error: ErrorEntity) : DetailAction()

class DetailStateMachine @Inject constructor(
    private val getRepoUseCase: GetRepoUseCase,
    private val dataFormatter: DataFormatter
) {

    val input: Relay<DetailAction> = PublishRelay.create()

    val state = input
        .doOnNext { Log.d(TAG, "received action $it") }
        .reduxStore(
            initialState = DetailState(),
            sideEffects = listOf(
                loadDataSideEffect()
            ),
            reducer = this::reducer
        )
        .distinctUntilChanged()

    fun loadDataSideEffect(): SideEffect<DetailState, DetailAction> =
        { actions, _ ->
            actions.ofType(DetailAction.LoadRepoDetail::class.java)
                .switchMap { action ->
                    getRepoUseCase.get(action.id)
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map {
                            when (it) {
                                is Resource.Success ->
                                    DetailLoadedAction(
                                        it.data
                                    )
                                is Resource.Error ->
                                    DetailLoadErrorAction(
                                        it.error
                                    )
                            }
                        }
                }
        }

    fun reducer(state: DetailState, action: DetailAction): DetailState {
        return when (action) {
            is DetailLoadedAction -> state.copy(
                viewData = action.entity.toViewData(dataFormatter),
                errorMessage = null
            )
            is DetailLoadErrorAction -> state.copy(
                viewData = null,
                errorMessage = action.error.message
            )
            else -> state
        }
    }
}