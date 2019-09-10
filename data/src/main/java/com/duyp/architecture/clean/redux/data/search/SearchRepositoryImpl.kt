package com.duyp.architecture.clean.redux.data.search

import com.duyp.architecture.clean.redux.data.api.SearchApi
import com.duyp.architecture.clean.redux.data.api.toEntity
import com.duyp.architecture.clean.redux.data.repo.RepoApiData
import com.duyp.architecture.clean.redux.data.repo.toLocal
import com.duyp.architecture.clean.redux.domain.ListEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.duyp.architecture.clean.redux.domain.search.SearchRepository
import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import io.reactivex.Single
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val mSearchApi: SearchApi,
    private val mRepoDao: RepoDao
) : SearchRepository {

    override fun searchPublicRepositories(term: String, page: Int): Single<ListEntity<RepoEntity>> {
        return mSearchApi.searchRepositories(term, page)
            .doOnSuccess {
                it.items.forEach { repo ->
                    mRepoDao.insert(repo.toLocal())
                }
            }
            .map {
                it.toEntity<RepoEntity, RepoApiData>()
            }
    }
}
