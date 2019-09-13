package com.duyp.architecture.clean.redux.domain.recentrepo

import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import java.util.*

data class RecentRepoEntity(
    val repo: RepoEntity,
    val dateTime: Date
)
