package com.duyp.architecture.clean.redux.data.recentrepo

import androidx.room.Dao
import androidx.room.Query
import com.duyp.architecture.clean.redux.data.database.BaseDao
import io.reactivex.Single

@Dao
abstract class RecentRepoDao : BaseDao<RecentRepoLocalData>() {

    @Query("SELECT * FROM RecentRepository WHERE repo_fullName like :query")
    abstract fun getRecentRepos(query: String): Single<List<RecentRepoLocalData>>

    @Query("SELECT * FROM RecentRepository WHERE repo_id = :id")
    abstract fun getRecentRepoById(id: Long): Single<RecentRepoLocalData>
}
