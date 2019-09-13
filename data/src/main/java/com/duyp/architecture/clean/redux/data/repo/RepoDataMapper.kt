package com.duyp.architecture.clean.redux.data.repo

import com.duyp.architecture.clean.redux.data.database.UserLocalData
import com.duyp.architecture.clean.redux.data.user.UserApiData

fun RepoApiData.toLocal(): RepoLocalData {
    return RepoLocalData(
        id = this.id,
        owner = this.owner?.toLocal(),
        name = this.name,
        fullName = this.fullName,
        htmlUrl = this.htmlUrl,
        description = this.description,
        url = this.url,
        homepage = this.homepage,
        defaultBranch = this.defaultBranch,
        language = this.language,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        pushedAt = this.pushedAt,
        size = this.size,
        stargazersCount = this.stargazersCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount,
        openIssuesCount = this.openIssuesCount,
        forks = this.forks,
        openIssues = this.openIssues,
        watchers = this.watchers,
        private = this.private,
        fork = this.fork,
        hasIssues = this.hasIssues,
        hasProjects = this.hasProjects,
        hasDownloads = this.hasDownloads,
        hasWiki = this.hasWiki,
        hasPages = this.hasPages
    )
}

fun UserApiData.toLocal(): UserLocalData {
    val localData = UserLocalData(this.id)
    localData.login = this.login
    localData.avatarUrl = this.avatarUrl
    localData.gravatarId = this.gravatarId
    localData.url = this.url
    localData.htmlUrl = this.htmlUrl
    localData.followersUrl = this.followersUrl
    localData.followingUrl = this.followingUrl
    localData.gistsUrl = this.gistsUrl
    localData.starredUrl = this.starredUrl
    localData.subscriptionsUrl = this.subscriptionsUrl
    localData.organizationsUrl = this.organizationsUrl
    localData.reposUrl = this.reposUrl
    localData.eventsUrl = this.eventsUrl
    localData.receivedEventsUrl = this.receivedEventsUrl
    localData.type = this.type
    localData.siteAdmin = this.siteAdmin
    localData.name = this.name
    localData.company = this.company
    localData.blog = this.blog
    localData.location = this.location
    localData.email = this.email
    localData.hireable = this.hireable
    localData.bio = this.bio
    localData.publicRepos = this.publicRepos
    localData.publicGists = this.publicGists
    localData.followers = this.followers
    localData.following = this.following
    localData.createdAt = this.createdAt
    localData.updatedAt = this.updatedAt
    localData.privateGists = this.privateGists
    localData.totalPrivateRepos = this.totalPrivateRepos
    localData.ownedPrivateRepos = this.ownedPrivateRepos
    localData.diskUsage = this.diskUsage
    localData.collaborators = this.collaborators
    localData.twoFactorAuthentication = this.twoFactorAuthentication
    localData.contributions = this.contributions
    return localData
}
