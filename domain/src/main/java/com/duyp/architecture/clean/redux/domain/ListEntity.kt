package com.duyp.architecture.clean.redux.domain

interface ListEntity<T> {

    fun items(): List<T>

    fun totalCount(): Long

    fun hasMore(): Boolean
}