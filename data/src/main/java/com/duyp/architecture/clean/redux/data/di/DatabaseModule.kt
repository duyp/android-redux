package com.duyp.architecture.clean.redux.data.di

import android.content.Context
import androidx.room.Room
import com.duyp.architecture.clean.redux.data.database.AppDatabase
import com.duyp.architecture.clean.redux.repo.data.database.RepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "android-redux")
            .build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideRepoDao(database: AppDatabase): RepoDao = database.repoDao()
}
