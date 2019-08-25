package com.duyp.architecture.clean.redux.shareddata.api

import com.duyp.architecture.clean.redux.shareddomain.ListEntity

fun <EntityType, ApiType : EntityType> PageableApiData<ApiType>.toEntity(): ListEntity<EntityType> {
    return object : ListEntity<EntityType> {

        override fun items(): List<EntityType> = items

        override fun totalCount(): Long = totalCount ?: 0

        override fun nextPage(): Int = next ?: 0

        override fun lastPage(): Int = last ?: 0
    }
}