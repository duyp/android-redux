package com.duyp.architecture.clean.redux.app.di

import com.duyp.architecture.clean.redux.app.features.search.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ContributesAndroidInjector
    fun contributeSearchActivity(): SearchActivity
}
