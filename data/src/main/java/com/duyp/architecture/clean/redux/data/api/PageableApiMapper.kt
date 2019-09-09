package com.duyp.architecture.clean.redux.data.api

import com.duyp.architecture.clean.redux.domain.ListEntity

fun <EntityType, ApiType : EntityType> PageableApiData<ApiType>.toEntity(): ListEntity<EntityType> {

    return object : ListEntity<EntityType> {

        override fun items(): List<EntityType> = items

        override fun totalCount(): Long = totalCount ?: 0

        override fun hasMore(): Boolean {
            if (next == null)
                return false
            val lastPage = last ?: 0
            return next!! <= lastPage
        }
    }
}
