package com.duyp.architecture.clean.redux.repo.domain

import javax.inject.Inject

class GetRepoUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {

    fun get(id: Long) = repoRepository.getRepo(id)
}