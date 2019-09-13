package com.duyp.architecture.clean.redux.data.recentrepo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.duyp.architecture.clean.redux.data.repo.RepoLocalData
import java.util.*

@Entity(
    tableName = "RecentRepository", foreignKeys = [ForeignKey(
        entity = RepoLocalData::class,
        parentColumns = ["id"],
        childColumns = ["repoId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class RecentRepoLocalData(

    @PrimaryKey val repoId: Long,

    val dateTime: Date
)
