package com.duyp.architecture.clean.redux.app.di.modules

import com.duyp.architecture.clean.redux.app.di.scopes.ActivityScope
import com.duyp.architecture.clean.redux.app.features.detail.DetailActivity
import com.duyp.architecture.clean.redux.app.features.search.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    fun contributeSearchActivity(): SearchActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun contributeDetailActivity(): DetailActivity
}
