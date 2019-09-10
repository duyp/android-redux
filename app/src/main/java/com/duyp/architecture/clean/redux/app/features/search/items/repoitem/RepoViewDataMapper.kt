package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

fun RepoEntity.toViewData() = RepoViewData(
    id = id(),
    name = name(),
    fullName = fullName(),
    description = description(),
    ownerAvatarUrl = ownerAvatarUrl(),
    fork = fork(),
    private = private(),
    size = size(),
    stargazersCount = stargazersCount(),
    forks = forks(),
    language = language(),
    updatedAt = updatedAt()
)
