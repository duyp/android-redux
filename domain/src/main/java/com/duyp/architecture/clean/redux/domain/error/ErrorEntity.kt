package com.duyp.architecture.clean.redux.domain.error

sealed class ErrorEntity {

    abstract val originalThrowable: Throwable

    data class NetworkError(override val originalThrowable: Throwable) : ErrorEntity()

    data class UnknownError(override val originalThrowable: Throwable) : ErrorEntity()
}
