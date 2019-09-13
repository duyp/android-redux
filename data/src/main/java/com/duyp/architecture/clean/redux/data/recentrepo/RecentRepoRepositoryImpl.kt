package com.duyp.architecture.clean.redux.repo.data

import com.duyp.architecture.clean.redux.data.recentrepo.RecentRepoDao
import com.duyp.architecture.clean.redux.data.recentrepo.RecentRepoLocalData
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoEntity
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoRepository
import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class RecentRepoRepositoryImpl @Inject constructor(
    private val recentRepoDao: RecentRepoDao,
    private val repoDao: RepoDao
) : RecentRepoRepository {

    override fun getRecentRepos(query: String): Single<List<RecentRepoEntity>> {
        return recentRepoDao.getRecentRepos("%$query%")
            .flattenAsObservable { it }
            .flatMapSingle { recentRepoLocal ->
                repoDao.getById(recentRepoLocal.repoId)
                    .map {
                        RecentRepoEntity(
                            it,
                            recentRepoLocal.dateTime
                        )
                    }
            }
            .toList()
    }

    override fun addRecentRepo(repoId: Long, dateTime: Date): Completable {
        return Completable.fromAction {
            recentRepoDao.insert(
                RecentRepoLocalData(repoId, dateTime)
            )
        }
    }

}
