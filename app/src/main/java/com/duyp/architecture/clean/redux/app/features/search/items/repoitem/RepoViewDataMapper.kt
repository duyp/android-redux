package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.utils.ColorGenerator
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

fun RepoEntity.toViewData(dataFormatter: DataFormatter) =
    RepoViewData(
        id = id(),
        name = name(),
        fullName = fullName(),
        description = description() ?: "No description",
        ownerAvatarUrl = ownerAvatarUrl(),
        isFork = fork(),
        isPrivate = private(),
        size = dataFormatter.getFormattedFileSize(sizeInByte = size() * 1024),
        stargazersCount = stargazersCount(),
        forks = forks(),
        language = language(),
        languageColor = ColorGenerator.getColor(language()),
        updatedAt = updatedAt()
    )
