package com.duyp.architecture.clean.redux.shareddomain

interface ListEntity<T> {

    fun items(): List<T>

    fun totalCount(): Long

    fun nextPage(): Int

    fun lastPage(): Int
}