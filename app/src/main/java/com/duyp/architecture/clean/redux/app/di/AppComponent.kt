package com.duyp.architecture.clean.redux.app.di

import com.duyp.architecture.clean.redux.app.App
import com.duyp.architecture.clean.redux.data.di.ApiModule
import com.duyp.architecture.clean.redux.data.di.DatabaseModule
import com.duyp.architecture.clean.redux.data.di.RepositoryBindingModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApiModule::class,
        DatabaseModule::class,
        RepositoryBindingModule::class,
        ActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<App>
