package com.duyp.architecture.clean.redux.data.api

import com.duyp.architecture.clean.redux.domain.ListEntity

fun <EntityType, ApiType : EntityType> PageableApiData<ApiType>.toEntity(): ListEntity<EntityType> {
    return object : ListEntity<EntityType> {

        override fun items(): List<EntityType> = items

        override fun totalCount(): Long = totalCount ?: 0

        override fun nextPage(): Int = next ?: 0

        override fun lastPage(): Int = last ?: 0
    }
}