package com.duyp.architecture.clean.redux.domain.recentrepo

import io.reactivex.Completable
import io.reactivex.Single

interface RecentRepoRepository {

    fun getRecentRepos(query: String): Single<List<RecentRepoEntity>>

    fun addRecentRepo(entity: RecentRepoEntity): Completable
}
