package com.duyp.architecture.clean.redux.data.recentrepo

import com.duyp.architecture.clean.redux.data.repo.toLocal
import com.duyp.architecture.clean.redux.domain.recentrepo.RecentRepoEntity

fun RecentRepoLocalData.toEntity() = RecentRepoEntity(
    this.repo, this.dateTime
)

fun RecentRepoEntity.toLocal() = RecentRepoLocalData(
    this.repo.toLocal(), this.dateTime
)
