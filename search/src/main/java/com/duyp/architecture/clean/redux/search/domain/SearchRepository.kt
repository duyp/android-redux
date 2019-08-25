package com.duyp.architecture.clean.redux.search.domain

import com.duyp.architecture.clean.redux.repo.domain.RepoEntity
import com.duyp.architecture.clean.redux.shareddomain.ListEntity
import io.reactivex.Single

interface SearchRepository {

    /**
     * Search public repositories with github api
     */
    fun searchPublicRepositories(term: String, page: Int): Single<ListEntity<RepoEntity>>
}