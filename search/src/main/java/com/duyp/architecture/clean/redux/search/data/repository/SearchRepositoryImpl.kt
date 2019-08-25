package com.duyp.architecture.clean.redux.search.data.repository

import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import com.duyp.architecture.clean.redux.repo.data.toLocal
import com.duyp.architecture.clean.redux.repo.domain.RepoEntity
import com.duyp.architecture.clean.redux.search.data.api.SearchApi
import com.duyp.architecture.clean.redux.search.domain.SearchRepository
import com.duyp.architecture.clean.redux.shareddata.api.toEntity
import com.duyp.architecture.clean.redux.shareddomain.ListEntity
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
                it.toEntity()
            }
    }
}