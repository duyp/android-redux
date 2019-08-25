package com.duyp.architecture.clean.redux.data.repo

import com.duyp.architecture.clean.redux.data.user.UserApiData
import com.duyp.architecture.clean.redux.domain.repo.RepoEntity
import com.google.gson.annotations.SerializedName
import java.util.*

data class RepoApiData(

    @SerializedName("id")
    val id: Long,

    @SerializedName("owner")
    val owner: UserApiData? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("html_url")
    val htmlUrl: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("homepage")
    val homepage: String? = null,

    @SerializedName("default_branch")
    val defaultBranch: String? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("created_at")
    val createdAt: Date? = null,

    @SerializedName("updated_at")
    val updatedAt: Date? = null,

    @SerializedName("pushed_at")
    val pushedAt: Date? = null,

    @SerializedName("size")
    val size: Long = 0,

    @SerializedName("stargazers_count")
    val stargazersCount: Long = 0,

    @SerializedName("watchers_count")
    val watchersCount: Long = 0,

    @SerializedName("forks_count")
    val forksCount: Long = 0,

    @SerializedName("open_issues_count")
    val openIssuesCount: Long = 0,

    @SerializedName("forks")
    val forks: Long = 0,

    @SerializedName("open_issues")
    val openIssues: Long = 0,

    @SerializedName("watchers")
    val watchers: Long = 0,

    @SerializedName("private")
    val private: Boolean = false,

    @SerializedName("fork")
    val fork: Boolean = false,

    @SerializedName("has_issues")
    val hasIssues: Boolean = false,

    @SerializedName("has_projects")
    val hasProjects: Boolean = false,

    @SerializedName("has_downloads")
    val hasDownloads: Boolean = false,

    @SerializedName("has_wiki")
    val hasWiki: Boolean = false,

    @SerializedName("has_pages")
    val hasPages: Boolean = false
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
