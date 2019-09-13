package com.duyp.architecture.clean.redux.domain.recentrepo

import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import javax.inject.Inject

class GetSingleRecentRepo @Inject constructor(
    private val repository: RecentRepoRepository,
    private val errorHandlingService: ErrorHandlingService
) {

    fun get(repoId: Long) =
        repository.getRecentRepoById(repoId)
            .map<Resource<RecentRepoEntity>> {
                Resource.Success(it)
            }
            .onErrorReturn {
                Resource.Error(errorHandlingService.getError(it))
            }
}
