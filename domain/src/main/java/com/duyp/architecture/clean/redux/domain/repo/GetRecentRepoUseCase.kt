package com.duyp.architecture.clean.redux.domain.repo

import javax.inject.Inject

class GetRecentRepoUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {

    fun get(query: String) = repoRepository.getRecentRepos(query)
}