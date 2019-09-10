package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import java.util.*

data class RepoViewData(
    val id: Long,
    val name: String?,
    val fullName: String?,
    val description: String?,
    val ownerAvatarUrl: String?,
    val fork: Boolean,
    val private: Boolean,
    val size: Long,
    val stargazersCount: Long,
    val forks: Long,
    val language: String?,
    val updatedAt: Date?
)
