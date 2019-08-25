package com.duyp.architecture.clean.redux.repo.data

import com.duyp.architecture.clean.redux.repo.data.database.RepoLocalData
import com.duyp.architecture.clean.redux.shareddata.api.UserApiData
import com.duyp.architecture.clean.redux.shareddata.database.UserLocalData


fun com.duyp.architecture.clean.redux.repo.data.api.RepoApiData.toLocal(): RepoLocalData {
    val localData = RepoLocalData(this.id)
    this.owner?.let { localData.owner = it.toLocal() }
    localData.name = this.name
    localData.fullName = this.fullName
    localData.htmlUrl = this.htmlUrl
    localData.description = this.description
    localData.url = this.url
    localData.homepage = this.homepage
    localData.defaultBranch = this.defaultBranch
    localData.language = this.language
    localData.createdAt = this.createdAt
    localData.updatedAt = this.updatedAt
    localData.pushedAt = this.pushedAt
    localData.size = this.size
    localData.stargazersCount = this.stargazersCount
    localData.watchersCount = this.watchersCount
    localData.forksCount = this.forksCount
    localData.openIssuesCount = this.openIssuesCount
    localData.forks = this.forks
    localData.openIssues = this.openIssues
    localData.watchers = this.watchers
    localData.private = this.private
    localData.fork = this.fork
    localData.hasIssues = this.hasIssues
    localData.hasProjects = this.hasProjects
    localData.hasDownloads = this.hasDownloads
    localData.hasWiki = this.hasWiki
    localData.hasPages = this.hasPages
    return localData
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