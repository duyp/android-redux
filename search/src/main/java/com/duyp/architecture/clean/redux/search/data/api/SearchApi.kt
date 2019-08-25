package com.duyp.architecture.clean.redux.search.data.api

import com.duyp.architecture.clean.redux.shareddata.api.PageableApiData
import com.duyp.architecture.clean.redux.shareddata.api.UserApiData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/repositories")
    fun searchRepositories(
        @Query(value = "q", encoded = true) query: String,
        @Query("page") page: Int
    ): Single<PageableApiData<com.duyp.architecture.clean.redux.repo.data.api.RepoApiData>>

    //    @GET("search/issues")
    //    fun searchIssues(@Query(value = "q", encoded = true) query: String, @Query("page") page: Int):
    //      Single<PageableApiData<IssueApiData>>
    //

    @GET("search/users")
    fun searchUsers(
        @Query(value = "q", encoded = true) query: String,
        @Query("page") page: Int
    ): Single<PageableApiData<UserApiData>>
}