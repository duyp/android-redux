package com.duyp.architecture.clean.redux.app.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.immoscout24.ImmoScout24.v4.injection.viewmodelfactory.ReduxAppViewModelFactory
import ch.immoscout24.ImmoScout24.v4.injection.viewmodelfactory.ViewModelKey
import com.duyp.architecture.clean.redux.app.features.detail.DetailViewModel
import com.duyp.architecture.clean.redux.app.features.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module used to define the connection between the framework's [ViewModelProvider.Factory] and
 * our own implementation: [ReduxAppViewModelFactory].
 */
@Module
interface ViewModelFactoryModule {

    @Binds
    fun bindViewModelFactory(factory: ReduxAppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(vm: SearchViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(vm: DetailViewModel): ViewModel
}
