package com.duyp.architecture.clean.redux.domain.recentrepo

import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import javax.inject.Inject

class GetRecentRepos @Inject constructor(
    private val repository: RecentRepoRepository,
    private val errorHandlingService: ErrorHandlingService
) {

    fun get(query: String) =
        repository.getRecentRepos(query)
            .map<Resource<List<RecentRepoEntity>>> {
                Resource.Success(it)
            }
            .onErrorReturn {
                Resource.Error(errorHandlingService.getError(it))
            }
}
