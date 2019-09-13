package com.duyp.architecture.clean.redux.repo.data

import com.duyp.architecture.clean.redux.data.recentrepo.RecentRepoDao
import com.duyp.architecture.clean.redux.data.recentrepo.toEntity
import com.duyp.architecture.clean.redux.data.recentrepo.toLocal
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoEntity
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RecentRepoRepositoryImpl @Inject constructor(
    private val recentRepoDao: RecentRepoDao
) : RecentRepoRepository {

    override fun getRecentRepos(query: String): Single<List<RecentRepoEntity>> {
        return recentRepoDao.getRecentRepos("%$query%")
            .flattenAsObservable { it }
            .map {
                it.toEntity()
            }
            .toList()
    }

    override fun getRecentRepoById(repoId: Long): Single<RecentRepoEntity> {
        return recentRepoDao.getRecentRepoById(repoId)
            .map { it.toEntity() }
    }

    override fun addRecentRepo(entity: RecentRepoEntity): Completable {
        return Completable.fromAction {
            recentRepoDao.insert(entity.toLocal())
        }
    }

}
