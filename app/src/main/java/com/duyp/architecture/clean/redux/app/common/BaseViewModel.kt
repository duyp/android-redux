package com.duyp.architecture.clean.redux.app.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<S, A> : ViewModel() {

    private val mutableState = MutableLiveData<S>()

    protected val disposables = CompositeDisposable()

    val state: LiveData<S> = mutableState

    fun initStateMachine(state: Observable<S>) {
        // observe state from the machine and convert it to live data
        state
            // important to set live data values on main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { mutableState.value = it }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}