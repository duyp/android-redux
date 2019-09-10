package com.duyp.architecture.clean.redux.app.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duyp.architecture.clean.redux.app.common.addTo
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchAction
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchState
import com.duyp.architecture.clean.redux.app.features.search.redux.SearchStateMachine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val stateMachine: SearchStateMachine) :
    ViewModel() {

    private val mutableState = MutableLiveData<SearchState>()

    private val disposables = CompositeDisposable()

    val state: LiveData<SearchState> = mutableState

    init {
        // observe state from the machine and convert it to live data
        stateMachine.state
            // important to set live data values on main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state -> mutableState.value = state }
            .addTo(disposables)
    }

    fun doAction(action: SearchAction) {
        stateMachine.input.accept(action)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}
