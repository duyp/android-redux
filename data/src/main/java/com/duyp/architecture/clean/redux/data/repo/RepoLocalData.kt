package com.duyp.architecture.clean.redux.data.repo

import androidx.room.*
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

    @Ignore
    override fun id(): Long = id

    @Ignore
    override fun ownerAvatarUrl(): String? = owner?.avatarUrl

    @Ignore
    override fun ownerLogin(): String? = owner?.login

    @Ignore
    override fun name(): String? = name

    @Ignore
    override fun fullName(): String? = fullName

    @Ignore
    override fun htmlUrl(): String? = htmlUrl

    @Ignore
    override fun description(): String? = description

    @Ignore
    override fun url(): String? = url

    @Ignore
    override fun homepage(): String? = homepage

    @Ignore
    override fun defaultBranch(): String? = defaultBranch

    @Ignore
    override fun language(): String? = language

    @Ignore
    override fun createdAt(): Date? = createdAt

    @Ignore
    override fun updatedAt(): Date? = updatedAt

    @Ignore
    override fun pushedAt(): Date? = pushedAt

    @Ignore
    override fun size(): Long = size

    @Ignore
    override fun stargazersCount(): Long = stargazersCount

    @Ignore
    override fun watchersCount(): Long = watchersCount

    @Ignore
    override fun forksCount(): Long = forksCount

    @Ignore
    override fun openIssuesCount(): Long = openIssuesCount

    @Ignore
    override fun forks(): Long = forks

    @Ignore
    override fun openIssues(): Long = openIssues

    @Ignore
    override fun watchers(): Long = watchers

    @Ignore
    override fun private(): Boolean = private

    @Ignore
    override fun fork(): Boolean = fork

    @Ignore
    override fun hasIssues(): Boolean = hasIssues

    @Ignore
    override fun hasProjects(): Boolean = hasProjects

    @Ignore
    override fun hasDownloads(): Boolean = hasDownloads

    @Ignore
    override fun hasWiki(): Boolean = hasWiki

    @Ignore
    override fun hasPages(): Boolean = hasPages
}
