package com.duyp.architecture.clean.redux.repo.domain

import java.util.*

interface RepoEntity {

    fun id(): Long

    fun ownerAvatarUrl(): String?

    fun ownerLogin(): String?

    fun name(): String?

    fun fullName(): String?

    fun htmlUrl(): String?

    fun description(): String?

    fun url(): String?

    fun homepage(): String?

    fun defaultBranch(): String?

    fun language(): String?

    fun createdAt(): Date?

    fun updatedAt(): Date?

    fun pushedAt(): Date?

    fun size(): Long

    fun stargazersCount(): Long

    fun watchersCount(): Long

    fun forksCount(): Long

    fun openIssuesCount(): Long

    fun forks(): Long

    fun openIssues(): Long

    fun watchers(): Long

    fun private(): Boolean

    fun fork(): Boolean

    fun hasIssues(): Boolean

    fun hasProjects(): Boolean

    fun hasDownloads(): Boolean

    fun hasWiki(): Boolean

    fun hasPages(): Boolean
}