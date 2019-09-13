package com.duyp.architecture.clean.redux.data.recentrepo

import androidx.room.Dao
import androidx.room.Query
import com.duyp.architecture.clean.redux.data.database.BaseDao
import io.reactivex.Single

@Dao
abstract class RecentRepoDao : BaseDao<RecentRepoLocalData>() {

    @Query("SELECT * FROM RecentRepository INNER JOIN Repository WHERE RecentRepository.repoId = Repository.id AND Repository.fullName like :query")
    abstract fun getRecentRepos(query: String): Single<List<RecentRepoLocalData>>

}
