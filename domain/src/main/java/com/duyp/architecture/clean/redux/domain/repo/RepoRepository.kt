package com.duyp.architecture.clean.redux.domain.repo

import io.reactivex.Single

interface RepoRepository {

    fun getRepo(id: Long): Single<RepoEntity>
}
