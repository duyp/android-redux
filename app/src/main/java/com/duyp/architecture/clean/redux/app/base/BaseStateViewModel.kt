package com.duyp.architecture.clean.redux.app.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

abstract class BaseStateViewModel<S, A> : ViewModel() {

    private val inputRelay: Relay<A> = PublishRelay.create()

    private val mutableState = MutableLiveData<S>()

    protected val disposables = CompositeDisposable()

    val state: LiveData<S> = mutableState

    val input: Consumer<A> = inputRelay

    /**
     * Init the view model with input and state. Must be called in constructor of child classes
     *
     * @param inputConsumer consume actions emitted by [input] for further processes
     *
     * @param stateObservable observable emitting states which will be passed to [state] live data to update the view
     */
    protected fun init(inputConsumer: Consumer<A>, stateObservable: Observable<S>) {
        addDisposable {
            inputRelay.subscribe(inputConsumer)
        }
        addDisposable {
            stateObservable
                // important to set live data values on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state -> mutableState.value = state }
        }
    }

    protected inline fun addDisposable(disposable: () -> Disposable) {
        disposables.add(disposable())
    }

    open fun doAction(action: A) {
        inputRelay.accept(action)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
