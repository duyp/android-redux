package com.duyp.architecture.clean.redux.domain.search

import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import io.reactivex.Single

interface SearchRepository {

    /**
     * Search public repositories with github api
     */
    fun searchPublicRepositories(term: String, page: Int): Single<ListEntity<RepoEntity>>
}