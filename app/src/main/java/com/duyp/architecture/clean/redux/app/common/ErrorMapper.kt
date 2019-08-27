package com.duyp.architecture.clean.redux.app.common

import com.duyp.architecture.clean.redux.domain.error.ErrorEntity

fun ErrorEntity.getMessage(): String {
    return when (this) {
        is ErrorEntity.NetworkError -> "Network error!"
        is ErrorEntity.UnknownError -> "Something went wrong, please try again later!"
    }
}