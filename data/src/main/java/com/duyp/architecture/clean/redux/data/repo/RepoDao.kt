package com.duyp.architecture.clean.redux.repo.data.database

import androidx.room.Dao
import androidx.room.Query
import com.duyp.architecture.clean.redux.data.database.BaseDao
import io.reactivex.Single

@Dao
abstract class RepoDao : BaseDao<RepoLocalData>() {

    @Query("SELECT id FROM Repository WHERE owner_login = :username")
    abstract fun getUserRepoIds(username: String): Single<List<Long>>

    @Query("SELECT COUNT(*) FROM Repository WHERE id = :id")
    abstract fun countRepo(id: Long): Int

    @Query("SELECT * FROM Repository WHERE id = :id")
    abstract fun getById(id: Long): Single<RepoLocalData>
}