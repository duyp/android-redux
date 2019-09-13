package com.duyp.architecture.clean.redux.data.recentrepo

import androidx.room.Embedded
import androidx.room.Entity
import com.duyp.architecture.clean.redux.data.repo.RepoLocalData
import java.util.*

@Entity(
    tableName = "RecentRepository", primaryKeys = ["repo_id"]
)
data class RecentRepoLocalData(

    @Embedded(prefix = "repo_")
    val repo: RepoLocalData,

    val dateTime: Date
)
