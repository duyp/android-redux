package com.duyp.architecture.clean.redux.domain.error

sealed class ErrorEntity(val message: String) {

    abstract val originalThrowable: Throwable

    data class NetworkError(override val originalThrowable: Throwable) : ErrorEntity(
        message = "No internet connection (${originalThrowable.message})"
    )

    data class RequestLimitedError(override val originalThrowable: Throwable) : ErrorEntity(
        message = "You've sent too much request, please patient! (${originalThrowable.message})"
    )

    class UnknownError(override val originalThrowable: Throwable) : ErrorEntity(
        message = "Unknown error (${originalThrowable.message})"
    )
}
