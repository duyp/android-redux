package com.duyp.architecture.clean.redux.domain.repo

import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import javax.inject.Inject

class GetRepo @Inject constructor(
    private val repoRepository: RepoRepository,
    private val errorHandlingService: ErrorHandlingService
) {

    fun get(id: Long) = repoRepository.getRepo(id)
        .map<Resource<RepoEntity>> {
            Resource.Success(it)
        }
        .onErrorReturn {
            Resource.Error(errorHandlingService.getError(it))
        }
}
