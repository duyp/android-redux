package com.duyp.architecture.clean.redux.app.features.detail

import com.duyp.architecture.clean.redux.app.common.BaseViewModel
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val detailStateMachine: DetailStateMachine
) :
    BaseViewModel<DetailState, DetailAction>() {

    init {
        initStateMachine(detailStateMachine.state)
    }

    fun loadRepo(id: Long) {
        detailStateMachine.input.accept(DetailAction.LoadRepoDetail(id))
    }
}