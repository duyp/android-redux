package com.duyp.architecture.clean.redux.app.features.search.items.repoitem

import com.duyp.architecture.clean.redux.domain.repo.RepoEntity

fun RepoEntity.toViewData() = RepoViewData(id = id())