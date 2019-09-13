package com.duyp.architecture.clean.redux.domain.recentrepo

import com.duyp.architecture.clean.redux.domain.repo.RepoRepository
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class AddRecentRepo @Inject constructor(
    private val recentRepoRepository: RecentRepoRepository,
    private val repoRepository: RepoRepository
) {

    fun add(repoId: Long, dateTime: Date): Completable {
        return repoRepository.getRepo(repoId)
            .flatMapCompletable {
                recentRepoRepository.addRecentRepo(RecentRepoEntity(it, dateTime))
            }
    }
}
