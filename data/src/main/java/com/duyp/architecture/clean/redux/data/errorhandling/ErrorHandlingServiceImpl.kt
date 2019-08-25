package com.duyp.architecture.clean.redux.data.errorhandling

import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import java.io.IOException
import javax.inject.Inject

class ErrorHandlingServiceImpl @Inject constructor() :
    ErrorHandlingService {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.NetworkError(throwable)
            else -> ErrorEntity.UnknownError(throwable)
        }
    }
}