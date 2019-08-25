package com.duyp.architecture.clean.redux.data

import com.duyp.architecture.clean.redux.repo.database.RepoDao
import dagger.Module
import dagger.Provides

@Module
object DaoModule {

    @JvmStatic
    @Provides
    fun provideRepoDao(database: AppDatabase): com.duyp.architecture.clean.redux.repo.database.RepoDao =
        database.repoDao()

}