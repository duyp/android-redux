package com.duyp.architecture.clean.redux.domain

import com.duyp.architecture.clean.redux.domain.error.ErrorEntity

sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val error: ErrorEntity) : Resource<T>()
}