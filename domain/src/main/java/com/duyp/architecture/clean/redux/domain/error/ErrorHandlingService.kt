package com.duyp.architecture.clean.redux.domain.error

interface ErrorHandlingService {

    fun getError(throwable: Throwable): ErrorEntity
}