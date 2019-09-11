package com.duyp.architecture.clean.redux.app.features.search

import com.duyp.architecture.clean.redux.app.common.BaseViewModel
import com.duyp.architecture.clean.redux.app.common.addTo
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchNavigation
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchStateMachine
import com.duyp.architecture.clean.redux.app.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val stateMachine: SearchStateMachine) :
    BaseViewModel<SearchState, SearchAction>() {

    val navigation: SingleLiveEvent<SearchNavigation> = SingleLiveEvent()

    init {
        initStateMachine(stateMachine.state)

        stateMachine.navigation
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                navigation.value = it
            }
            .addTo(disposables)
    }

    fun doAction(action: SearchAction) {
        stateMachine.input.accept(action)
    }

}
