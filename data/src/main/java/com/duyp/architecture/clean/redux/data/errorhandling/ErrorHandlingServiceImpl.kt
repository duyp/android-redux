package com.duyp.architecture.clean.redux.data.errorhandling

import com.duyp.architecture.clean.redux.domain.error.ErrorEntity
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorHandlingServiceImpl @Inject constructor() :
    ErrorHandlingService {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.NetworkError(throwable)
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.RequestLimitedError(throwable)
                    else -> ErrorEntity.UnknownError(throwable)
                }
            }
            else -> ErrorEntity.UnknownError(throwable)
        }
    }
}
