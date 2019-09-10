package com.duyp.architecture.clean.redux.app.di

import android.content.Context
import com.duyp.architecture.clean.redux.app.ReduxApp
import com.duyp.architecture.clean.redux.app.di.modules.ActivityModule
import com.duyp.architecture.clean.redux.app.di.modules.ViewModelFactoryModule
import com.duyp.architecture.clean.redux.data.di.ApiModule
import com.duyp.architecture.clean.redux.data.di.DatabaseModule
import com.duyp.architecture.clean.redux.data.di.RepositoryBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApiModule::class,
        DatabaseModule::class,
        RepositoryBindingModule::class,
        ActivityModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AndroidInjector<ReduxApp> {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}
