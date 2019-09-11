package com.duyp.architecture.clean.redux.app.features.detail

import com.duyp.architecture.clean.redux.app.common.DataFormatter
import com.duyp.architecture.clean.redux.app.utils.ColorGenerator
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

fun RepoEntity.toViewData(dataFormatter: DataFormatter) =
    DetailViewData(
        id = id(),
        name = name() ?: "Unknown name",
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
