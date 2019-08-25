package com.duyp.architecture.clean.redux.repo.domain

import io.reactivex.Single

interface RepoRepository {

    fun getRepo(id: Long): Single<RepoEntity>
}