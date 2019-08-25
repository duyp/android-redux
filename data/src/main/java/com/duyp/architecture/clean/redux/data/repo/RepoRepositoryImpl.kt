package com.duyp.architecture.clean.redux.repo.data

import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import com.duyp.architecture.clean.redux.repo.domain.RepoEntity
import com.duyp.architecture.clean.redux.repo.domain.RepoRepository
import io.reactivex.Single
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoDao: RepoDao
) : RepoRepository {

    override fun getRepo(id: Long): Single<RepoEntity> {
        return repoDao.getById(id)
            .map { it }
    }
}