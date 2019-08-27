package com.duyp.architecture.clean.redux.app.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class BaseStateViewModel<S, A> : ViewModel() {

    private val inputSubject: Subject<A> = PublishSubject.create()

    private val mutableState = MutableLiveData<S>()

    private val disposables = CompositeDisposable()

    val state: LiveData<S> = mutableState

    /**
     * Init the view model with input actions and state. Must be called in constructor of child classes
     *
     * @param inputConsumer consume actions emitted by [inputSubject] for further processes
     *
     * @param stateObservable observable emitting states which will be passed to [state] live data to update the view
     */
    protected fun init(inputConsumer: Consumer<A>, stateObservable: Observable<S>) {
        addDisposable {
            inputSubject.subscribe(inputConsumer)
        }
        addDisposable {
            stateObservable
                // important to set live data values on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state -> mutableState.value = state }
        }
    }

    private inline fun addDisposable(disposable: () -> Disposable) {
        disposables.add(disposable())
    }

    fun doAction(action: A) {
        inputSubject.onNext(action)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
