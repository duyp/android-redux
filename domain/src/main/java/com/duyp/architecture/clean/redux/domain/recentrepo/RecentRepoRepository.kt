package com.duyp.architecture.clean.redux.domain.recentrepo

import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface RecentRepoRepository {

    fun getRecentRepos(query: String): Single<List<RecentRepoEntity>>

    fun addRecentRepo(repoId: Long, dateTime: Date): Completable

}
