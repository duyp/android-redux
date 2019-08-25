package com.duyp.architecture.clean.redux.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.duyp.architecture.clean.redux.repo.database.RepoDao
import com.duyp.architecture.clean.redux.repo.database.RepoLocalData

@Database(
    version = 1, exportSchema = true, entities = [
        com.duyp.architecture.clean.redux.repo.database.RepoLocalData::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): com.duyp.architecture.clean.redux.repo.database.RepoDao
}

