package com.duyp.architecture.clean.redux.data.database

import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import dagger.Module
import dagger.Provides

@Module
object DaoModule {

    @JvmStatic
    @Provides
    fun provideRepoDao(database: AppDatabase): RepoDao = database.repoDao()

}