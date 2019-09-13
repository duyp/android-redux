package com.duyp.architecture.clean.redux.data.repo

import com.duyp.architecture.clean.redux.data.user.UserApiData
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.google.gson.annotations.SerializedName
import java.util.*

data class RepoApiData(

    @SerializedName("id")
    var id: Long,

    @SerializedName("owner")
    var owner: UserApiData? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("full_name")
    var fullName: String? = null,

    @SerializedName("html_url")
    var htmlUrl: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("url")
    var url: String? = null,

    @SerializedName("homepage")
    var homepage: String? = null,

    @SerializedName("default_branch")
    var defaultBranch: String? = null,

    @SerializedName("language")
    var language: String? = null,

    @SerializedName("created_at")
    var createdAt: Date? = null,

    @SerializedName("updated_at")
    var updatedAt: Date? = null,

    @SerializedName("pushed_at")
    var pushedAt: Date? = null,

    @SerializedName("size")
    var size: Long = 0,

    @SerializedName("stargazers_count")
    var stargazersCount: Long = 0,

    @SerializedName("watchers_count")
    var watchersCount: Long = 0,

    @SerializedName("forks_count")
    var forksCount: Long = 0,

    @SerializedName("open_issues_count")
    var openIssuesCount: Long = 0,

    @SerializedName("forks")
    var forks: Long = 0,

    @SerializedName("open_issues")
    var openIssues: Long = 0,

    @SerializedName("watchers")
    var watchers: Long = 0,

    @SerializedName("private")
    var private: Boolean = false,

    @SerializedName("fork")
    var fork: Boolean = false,

    @SerializedName("has_issues")
    var hasIssues: Boolean = false,

    @SerializedName("has_projects")
    var hasProjects: Boolean = false,

    @SerializedName("has_downloads")
    var hasDownloads: Boolean = false,

    @SerializedName("has_wiki")
    var hasWiki: Boolean = false,

    @SerializedName("has_pages")
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
