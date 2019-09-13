package com.duyp.architecture.clean.redux.domain.recentrepo

import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class AddRecentRepo @Inject constructor(
    private val recentRepoRepository: RecentRepoRepository
) {

    fun add(repoId: Long, dateTime: Date): Completable {
        return recentRepoRepository.addRecentRepo(repoId, dateTime)
    }
}
