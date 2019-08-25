package com.duyp.architecture.clean.redux.data.repo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.duyp.architecture.clean.redux.data.database.UserLocalData
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import java.util.*

@Entity(tableName = "Repository", indices = [Index("owner_login")])
data class RepoLocalData(

    @PrimaryKey
    var id: Long = 0,

    @Embedded(prefix = "owner_")
    var owner: UserLocalData? = null,

    var name: String? = null,

    var fullName: String? = null,

    var htmlUrl: String? = null,

    var description: String? = null,

    var url: String? = null,

    var homepage: String? = null,

    var defaultBranch: String? = null,

    var language: String? = null,

    var createdAt: Date? = null,

    var updatedAt: Date? = null,

    var pushedAt: Date? = null,

    var size: Long = 0,

    var stargazersCount: Long = 0,

    var watchersCount: Long = 0,

    var forksCount: Long = 0,

    var openIssuesCount: Long = 0,

    var forks: Long = 0,

    var openIssues: Long = 0,

    var watchers: Long = 0,

    var private: Boolean = false,

    var fork: Boolean = false,

    var hasIssues: Boolean = false,

    var hasProjects: Boolean = false,

    var hasDownloads: Boolean = false,

    var hasWiki: Boolean = false,

    var hasPages: Boolean = false

) : RepoEntity {

    override fun id(): Long = id

    override fun ownerAvatarUrl(): String? = owner?.avatarUrl

    override fun ownerLogin(): String? = owner?.login

    override fun name(): String? = name

    override fun fullName(): String? = fullName

    override fun htmlUrl(): String? = htmlUrl

    override fun description(): String? = description

    override fun url(): String? = url

    override fun homepage(): String? = homepage

    override fun defaultBranch(): String? = defaultBranch

    override fun language(): String? = language

    override fun createdAt(): Date? = createdAt

    override fun updatedAt(): Date? = updatedAt

    override fun pushedAt(): Date? = pushedAt

    override fun size(): Long = size

    override fun stargazersCount(): Long = stargazersCount

    override fun watchersCount(): Long = watchersCount

    override fun forksCount(): Long = forksCount

    override fun openIssuesCount(): Long = openIssuesCount

    override fun forks(): Long = forks

    override fun openIssues(): Long = openIssues

    override fun watchers(): Long = watchers

    override fun private(): Boolean = private

    override fun fork(): Boolean = fork

    override fun hasIssues(): Boolean = hasIssues

    override fun hasProjects(): Boolean = hasProjects

    override fun hasDownloads(): Boolean = hasDownloads

    override fun hasWiki(): Boolean = hasWiki

    override fun hasPages(): Boolean = hasPages
}