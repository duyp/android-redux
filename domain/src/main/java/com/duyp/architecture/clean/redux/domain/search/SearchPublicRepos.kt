package com.duyp.architecture.clean.redux.domain.search

import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.Resource
import com.duyp.architecture.clean.redux.domain.error.ErrorHandlingService
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import javax.inject.Inject

class SearchPublicRepos @Inject constructor(
    private val repository: SearchRepository,
    private val errorHandlingService: ErrorHandlingService
) {

    fun search(term: String, page: Int) =
        repository.searchPublicRepositories(term, page)
            .map<Resource<ListEntity<RepoEntity>>> {
                Resource.Success(it)
            }
            .onErrorReturn {
                Resource.Error(errorHandlingService.getError(it))
            }
}
