package com.duyp.architecture.clean.redux.shareddomain

interface ErrorHandlingService {

    fun getError(throwable: Throwable): ErrorEntity
}