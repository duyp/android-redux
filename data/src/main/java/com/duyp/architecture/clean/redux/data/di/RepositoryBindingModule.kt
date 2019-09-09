package com.duyp.architecture.clean.redux.data.di

import com.duyp.architecture.clean.redux.data.search.SearchRepositoryImpl
import com.duyp.architecture.clean.redux.domain.repo.RepoRepository
import com.duyp.architecture.clean.redux.domain.search.SearchRepository
import com.duyp.architecture.clean.redux.repo.data.RepoRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryBindingModule {

    @Binds
    fun bindRepoRepository(repo: RepoRepositoryImpl): RepoRepository

    @Binds
    fun bindSearchRepository(repo: SearchRepositoryImpl): SearchRepository
}
