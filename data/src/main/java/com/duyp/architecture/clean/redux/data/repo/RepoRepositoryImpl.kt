package com.duyp.architecture.clean.redux.repo.data

import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.duyp.architecture.clean.redux.domain.repo.RepoRepository
import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import io.reactivex.Single
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoDao: RepoDao
) : RepoRepository {

    override fun getRepo(id: Long): Single<RepoEntity> {
        return repoDao.getById(id)
            .map { it }
    }

    override fun getRecentRepos(query: String): Single<List<RepoEntity>> {
        return repoDao.getRecentRepos()
            .map { it }
    }
}
