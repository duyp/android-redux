package com.duyp.architecture.clean.redux.search.domain

import com.duyp.architecture.clean.redux.repo.domain.RepoEntity
import com.duyp.architecture.clean.redux.shareddomain.ErrorHandlingService
import com.duyp.architecture.clean.redux.shareddomain.ListEntity
import com.duyp.architecture.clean.redux.shareddomain.Resource
import javax.inject.Inject

class SearchPublicRepoUseCase @Inject constructor(
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