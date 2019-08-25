package com.duyp.architecture.clean.redux.domain.repo

import javax.inject.Inject

class GetRepoUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {

    fun get(id: Long) = repoRepository.getRepo(id)
}